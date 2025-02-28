package fr.isen.fayet.isensmartcompanion.models

import java.io.Serializable

class EventModel(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String
) :Serializable {
    companion object {
        fun fakeEvents(): List<EventModel> {
            return listOf(
                EventModel("1", "Soirée BDE", "Une soirée organisée par le BDE", "2025-03-10", "Salle des fêtes", "Festif"),
                EventModel("2", "Gala ISEN", "Le gala annuel de l'ISEN", "2025-04-15", "Hôtel de Ville", "Cérémonie"),
                EventModel("3", "Journée de cohésion", "Une journée d'intégration pour les étudiants", "2025-09-20", "Campus ISEN", "Social")
            )
        }
    }
}