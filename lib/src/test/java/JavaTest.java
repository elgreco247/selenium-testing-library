import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seleniumtestinglib.CoreKt.asJsExpression;
import static seleniumtestinglib.CoreKt.byRole;
import static seleniumtestinglib.DriverKt.getDriver;
import static seleniumtestinglib.DriverKt.render;
import static seleniumtestinglib.Role.Heading;
import static seleniumtestinglib.TL.testId;


public class JavaTest {

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
                byRole(Heading).name(asJsExpression("/something/"))
        );

        assertEquals("something as a user something", result.stream().findFirst().get().getAccessibleName());
    }
}
