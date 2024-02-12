package seleniumtestinglib.interactions

import org.junit.jupiter.api.extension.ExtendWith
import org.openqa.selenium.remote.RemoteWebDriver
import seleniumtestinglib.DriverLifeCycle
import seleniumtestinglib.locators.ByRole
import seleniumtestinglib.locators.Role
import seleniumtestinglib.locators.Role.LISTBOX
import seleniumtestinglib.render
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExtendWith(DriverLifeCycle::class)
class DeselectOptionsTest(private val driver: RemoteWebDriver) {

    @Test
    fun `deselect options`() {
        driver.render(
            """<select multiple>
                      <option value="1">A</option>
                      <option value="2">B</option>
                      <option value="3" selected>C</option>
                    </select>"""
        )
        assertTrue(driver.findElement(ByRole(Role.OPTION, name = "C")).isSelected)

        driver.user.deselectOptions(driver.findElement(ByRole(LISTBOX)), "3")

        assertFalse(driver.findElement(ByRole(Role.OPTION, name = "C")).isSelected)
    }

    @Test
    fun `deselect options by element`() {
        driver.render(
            """<select multiple>
                      <option value="1" selected>A</option>
                      <option value="2" selected>B</option>
                      <option value="3" selected>C</option>
                    </select>"""
        )
        val select = driver.findElement(ByRole(LISTBOX))

        driver.user.deselectOptions(
            select,
            driver.findElement(ByRole(Role.OPTION, name = "C")),
        )

        assertFalse(driver.findElement(ByRole(Role.OPTION, name = "C")).isSelected)
    }
}
