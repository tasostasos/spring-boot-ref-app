package com.example.demo.dto.keycloak.rest.response.users;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccessTest {

    @Test
    public void testAccessDTO() {
        Access access = new Access();
        access.setManageGroupMembership(true);
        access.setView(false);
        access.setMapRoles(true);
        access.setImpersonate(false);
        access.setManage(true);

        assertTrue(access.isManageGroupMembership());
        assertFalse(access.isView());
        assertTrue(access.isMapRoles());
        assertFalse(access.isImpersonate());
        assertTrue(access.isManage());

    }


}