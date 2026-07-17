package com.gabriel.tiziano.teamheritage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class TeamheritageApplicationTests {

	@Test
	void contextLoads() {
	}

}

