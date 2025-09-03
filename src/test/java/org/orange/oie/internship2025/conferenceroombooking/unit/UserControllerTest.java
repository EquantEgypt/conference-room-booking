package org.orange.oie.internship2025.conferenceroombooking.unit;

import org.orange.oie.internship2025.conferenceroombooking.configuration.SecurityConfiguration;
import org.orange.oie.internship2025.conferenceroombooking.controller.UserController;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

@WebMvcTest(UserController.class)
@Import(SecurityConfiguration.class)
public class UserControllerTest {

}
