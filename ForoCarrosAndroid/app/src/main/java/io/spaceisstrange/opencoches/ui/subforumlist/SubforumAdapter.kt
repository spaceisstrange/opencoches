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

package io.spaceisstrange.opencoches.ui.subforumlist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.spaceisstrange.opencoches.R
import io.spaceisstrange.opencoches.data.model.Subforum
import kotlinx.android.synthetic.main.list_item_subforum.view.*

/**
 * Adapter de los subforos
 */
class SubforumAdapter(val onClick: (subforum: Subforum) -> Unit) : RecyclerView.Adapter<SubforumAdapter.SubforumHolder>() {
    /**
     * Lista con los subforos
     */
    var subforums: MutableList<Subforum> = mutableListOf()

    /**
     * Actualiza la lista de subforos y notifica al Adapter sobre los cambios
     */
    fun update(subs: List<Subforum>) {
        subforums.clear()
        subforums.addAll(subs)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SubforumHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_subforum, parent, false)
        val holder = SubforumHolder(view)

        holder.itemView.llClickable.setOnClickListener {
            onClick(subforums[holder.adapterPosition])
        }

        return holder
    }

    override fun onBindViewHolder(holder: SubforumHolder, position: Int) {
        holder.bind(subforums[position])
    }

    override fun getItemCount(): Int {
        return subforums.size
    }

    /**
     * View Holder con la información de un subforo
     */
    class SubforumHolder(val view: View) : RecyclerView.ViewHolder(view) {
        /**
         * Actualiza el contenido del holder con un nuevo subforo
         */
        fun bind(subforum: Subforum) {
            itemView.tvSubforoIcon.text = subforum.title[0].toString()
            itemView.tvSubforoTitle.text = subforum.title
        }
    }
}