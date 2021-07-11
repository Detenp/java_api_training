package fr.lernejo.navy_battle;

import org.junit.jupiter.api.Test;

public class LauncherTest {
    @Test
    public void launcherTest() throws Exception{
        Launcher.main(new String[]{"9876"});
        Launcher.main(new String[]{"4567", "http://localhost:9876"});
    }
}
