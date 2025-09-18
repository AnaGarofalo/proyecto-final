package com.proyectofinal.proyectofinal;

import com.proyectofinal.proyectofinal.model.AppUser;
import com.proyectofinal.proyectofinal.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public abstract class AbstractTest {
    @Autowired
    private AppUserService appUserService;

    public AppUser getBaseUser() {
        return appUserService.getByEmail("admin@root.com");
    }
}
