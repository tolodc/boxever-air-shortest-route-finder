package com.boxever.air.shortestroute.adapter.shell;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT + ".enabled=false"
})
class RoutingShellIntegrationTest {

    @Autowired
    private Shell shell;

    @Test
    void contextLoads() {
        // Given
        final String givenCommand = "route DUB SYD";
        final String expectedPrint = "DUB -- LHR (1)\n" + "LHR -- BKK (9)\n" + "BKK -- SYD (11)\n" + "Time: 21";

        // When
        Object route_command = shell.evaluate(() -> givenCommand);

        // Then
        assertThat(route_command, is(expectedPrint));
    }

}
