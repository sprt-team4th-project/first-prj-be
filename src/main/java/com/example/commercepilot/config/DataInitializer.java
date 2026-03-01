package com.example.commercepilot.config;

import com.example.commercepilot.admin.entity.Admin;
import com.example.commercepilot.admin.entity.AdminRole;
import com.example.commercepilot.admin.entity.AdminStatus;
import com.example.commercepilot.admin.repository.AdminRepository;
import com.example.commercepilot.customer.entity.Customer;
import com.example.commercepilot.customer.entity.CustomerStatus;
import com.example.commercepilot.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        seedAdmins();
        seedCustomers();
    }

    private void seedAdmins() throws Exception {
        if (adminRepository.count() > 0) {
            log.info("[DataInitializer] Admin 데이터가 이미 존재합니다.");
            return;
        }

        String encodedAdminPw = passwordEncoder.encode("Admin1234");

        Admin superAdmin = createAdmin(
                "슈퍼관리자",
                "superadmin@test.com",
                encodedAdminPw,
                "010-1111-1111",
                AdminRole.SUPER_ADMIN,
                AdminStatus.ACTIVE
        );
        Admin savedSuper = adminRepository.save(superAdmin);
        log.info("[DataInitializer] SUPER_ADMIN 생성 완료 ");

        Admin opAdmin = createAdmin(
                "운영관리자",
                "op@test.com",
                encodedAdminPw,
                "010-2222-2222",
                AdminRole.OPERATION_ADMIN,
                AdminStatus.ACTIVE
        );
        opAdmin = setField(opAdmin, "superAdminId", savedSuper.getId());
        adminRepository.save(opAdmin);
        log.info("[DataInitializer] OPERATION_ADMIN 생성 완료 ");

        Admin csAdmin = createAdmin(
                "CS관리자",
                "cs@test.com",
                encodedAdminPw,
                "010-3333-3333",
                AdminRole.OPERATION_ADMIN,
                AdminStatus.PENDING
        );
        csAdmin = setField(csAdmin, "superAdminId", savedSuper.getId());
        adminRepository.save(csAdmin);
        log.info("[DataInitializer] CS_ADMIN 생성 완료 ");
    }

    private void seedCustomers() throws Exception {
        if (customerRepository.count() > 0) {
            log.info("[DataInitializer] Customer 데이터가 이미 존재합니다.");
            return;
        }

        String encodedCustomerPw = passwordEncoder.encode("Customer1234");

        Customer customer = new Customer();
        setField(customer, "customerName", "테스트유저");
        setField(customer, "email", "customer@test.com");
        setField(customer, "callNumber", "010-9999-9999");
        setField(customer, "password", encodedCustomerPw);
        setField(customer, "status", CustomerStatus.ACTIVE);

        customerRepository.save(customer);
        log.info("[DataInitializer] Customer 생성 완료 ");
    }

    private Admin createAdmin(String name, String email, String password,
                              String callNumber, AdminRole role, AdminStatus status) throws Exception {

        Admin admin = Admin.pending(name, email, password, callNumber, role);
        return setField(admin, "status", status);
    }

    @SuppressWarnings("unchecked")
    private <T> T setField(T obj, String fieldName, Object value) throws Exception {
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(obj, value);
                return obj;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }
}