package com.farmingapp.view.helper

open class TransformationUtil {

    fun transformListToString(list: List<Double>): String {
        val result = StringBuilder()

        for (value in list) {
            if (result.isEmpty()) {
                result.append(String.format("%.4f", value))
            } else {
                result.append("\n").append(String.format("%.4f", value))
            }
        }
        return result.toString()
    }

    fun transformStringToList(list: String): List<Double> {
        val arr = list.split(Regex("\n"), 0)
        return arr.map { it.toDouble() }
    }
}