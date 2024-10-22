package com.luczka.mycoffee.data.database.types

/**
 * Represents the different levels of coffee roast.
 *
 * - Light: Lightly roasted beans with a light brown color, usually having a higher acidity and pronounced origin flavors.
 * - Medium: Medium roasted beans with a balanced flavor profile, often with a bit more body and sweetness than light roast.
 * - MediumDark: Darker than medium roast, with more pronounced roast flavors and less acidity, sometimes with a hint of oil on the surface.
 * - Dark: Darkly roasted beans with a shiny black appearance, featuring strong roast flavors, bitterness, and less noticeable origin characteristics.
 *
 * @property order The order number for sorting the roast levels.
 */
enum class RoastType(val order: Int) {
    Light(1),
    Medium(2),
    MediumDark(3),
    Dark(4)
}
