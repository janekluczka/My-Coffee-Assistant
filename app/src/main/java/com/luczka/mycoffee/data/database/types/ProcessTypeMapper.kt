package com.luczka.mycoffee.data.database.types

/**
 * Represents the different coffee processing methods.
 *
 * - Natural: Also known as dry process, where the coffee cherries are dried with the fruit still attached, resulting in a fruity and complex flavor.
 * - Washed: Also known as wet process, where the fruit is removed before drying, leading to a cleaner and brighter cup with more acidity.
 * - Honey: A process that is between natural and washed, where some fruit pulp is left on the bean during drying, giving a balanced sweetness and body.
 * - Other: Any other processing method that does not fall into the common categories, such as anaerobic fermentation or experimental processes.
 */
enum class ProcessType {
    Natural,
    Washed,
    Honey,
    Other
}