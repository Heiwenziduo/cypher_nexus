package com.github.heiwenziduo.cypher_nexus.machinery

class CypherNotFoundException(override val message: String?) : Exception(message) {
}