/*
 * Hecho con <3 por Fran González (@spaceisstrange)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package io.spaceisstrange.opencoches.ui.search

import io.spaceisstrange.opencoches.data.api.search.Search
import io.spaceisstrange.opencoches.data.api.search.SearchPage
import io.spaceisstrange.opencoches.data.sharedpreferences.SharedPreferencesUtils
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class SearchPresenter @Inject constructor(val view: SearchContract.View,
                                          val sharedPreferencesUtils: SharedPreferencesUtils) : SearchContract.Presenter {
    /**
     * CompositeSubscription donde agregar todos los observables que vayamos utilizando
     */
    lateinit var compositeSubscription: CompositeSubscription

    /**
     * URL actual de la búsqueda para cuando queramos obtener las sucesivas páginas de la misma
     */
    var searchUrl: String? = null

    /**
     * Página actual de la búsqueda en la que nos encontramos
     */
    var actualPage = 1

    @Inject
    override fun setup() {
        view.setPresenter(this)
    }

    override fun init() {
        // Inicializamos la CompositeSubscription
        compositeSubscription = CompositeSubscription()
    }

    override fun search(query: String) {
        view.showLoading(true)
        val userId = sharedPreferencesUtils.getUserId()
        val searchSubscription = Search(userId, query).observable().subscribe(
                {
                    searchResults ->

                    view.showLoading(false)
                    view.setSearchPages(searchResults.pages)
                    view.showSearchResults(searchResults.results)

                    // Guardamos la URL para futuros usos
                    searchUrl = searchResults.searchUrl
                },
                {
                    error ->

                    view.showError(true)
                }
        )

        compositeSubscription.add(searchSubscription)
    }

    override fun loadNextSearchPage() {
        // Si no tenemos guardada la URL de búsqueda no tiene sentido estar por aquí >.<
        val url = searchUrl ?: return

        // Cargamos la nueva página
        actualPage++
        view.showLoading(true)
        val searchSubscription = SearchPage(url, actualPage).observable().subscribe(
                {
                    results ->

                    view.showLoading(false)
                    view.showMoreSearchResults(results)
                },
                {
                    error ->

                    view.showError(true)
                }
        )

        compositeSubscription.add(searchSubscription)
    }

    override fun finish() {
        compositeSubscription.unsubscribe()
    }
}