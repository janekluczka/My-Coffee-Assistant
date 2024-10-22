package com.luczka.mycoffee.data.database.types

/**
 * Represents the type of an electric coffee machine.
 *
 * Types include:
 * - Espresso: Traditional espresso machines for making espresso shots.
 * - Pod: Machines that use pods or capsules (e.g., Nespresso).
 * - Drip: Automatic drip coffee makers.
 */
enum class CoffeeMachineType {
    Espresso,
    Pod,
    Drip,
}