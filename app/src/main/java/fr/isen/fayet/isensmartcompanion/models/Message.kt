package fr.isen.fayet.isensmartcompanion.models

data class Message(
    val text: String, // Le contenu du message
    val isUser: Boolean // true pour l'utilisateur, false pour l'IA
)