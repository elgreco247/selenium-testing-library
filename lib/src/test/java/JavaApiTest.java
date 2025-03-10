import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seleniumtestinglib.CoreKt.asJsExpression;
import static seleniumtestinglib.DriverKt.getDriver;
import static seleniumtestinglib.DriverKt.render;
import static seleniumtestinglib.Role.Heading;
import static seleniumtestinglib.TL.*;


public class JavaApiTest {

    @Test
    public void byTestId() {
        render(getDriver(), "<div data-testid='custom' />");

        var result = getDriver().findElement(testId("custom"));
        assertEquals("div", result.getTagName());
    }

    @Test
    public void testByRole() {
        render(getDriver(), "<h1>something as a user something</h1>");

        var result = getDriver().findElements(
                role(Heading).name(asJsExpression("/something/")).level(1)
        );

        assertEquals("something as a user something", result.get(0).getAccessibleName());
    }


    @Test
    public void testByAltText() {
        render(getDriver(), "<img alt='Incredibles 2 Poster' src='/incredibles-2.png' />");

        var result = getDriver().findElements(altText("incredibles").exact(false));

        assertEquals("img", result.get(0).getTagName());
    }
}
