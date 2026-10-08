package com.dinechain;

import com.dinechain.constant.BranchStatus;
import com.dinechain.constant.StaffRole;
import com.dinechain.constant.StaffStatus;
import com.dinechain.entity.AreaEntity;
import com.dinechain.entity.BranchEntity;
import com.dinechain.entity.DiningTableEntity;
import com.dinechain.entity.BranchMenuItemEntity;
import com.dinechain.entity.MenuCategoryEntity;
import com.dinechain.entity.MenuItemEntity;
import com.dinechain.entity.StaffEntity;
import com.dinechain.repository.AreaRepository;
import com.dinechain.repository.BranchMenuItemRepository;
import com.dinechain.repository.BranchRepository;
import com.dinechain.repository.DiningTableRepository;
import com.dinechain.repository.MenuCategoryRepository;
import com.dinechain.repository.MenuItemRepository;
import com.dinechain.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Every test builds its own branches, menu and staff, so tests never see each other's data.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class IntegrationTestSupport {

    private static final AtomicInteger UNIQUE = new AtomicInteger();

    @Autowired protected MockMvc mockMvc;
    @Autowired protected BranchRepository branchRepository;
    @Autowired protected MenuCategoryRepository menuCategoryRepository;
    @Autowired protected MenuItemRepository menuItemRepository;
    @Autowired protected BranchMenuItemRepository branchMenuItemRepository;
    @Autowired protected StaffRepository staffRepository;
    @Autowired protected AreaRepository areaRepository;
    @Autowired protected DiningTableRepository diningTableRepository;

    @DynamicPropertySource
    static void jwtKeys(DynamicPropertyRegistry registry) throws Exception {
        TestJwtKeys.register(registry);
    }

    protected static int unique() {
        return UNIQUE.incrementAndGet();
    }

    protected BranchEntity createBranch(Integer kitchenCapacity) {
        int n = unique();
        BranchEntity branch = new BranchEntity();
        branch.setCode("T" + n);
        branch.setName("Test branch " + n);
        branch.setAddress(n + " Test street");
        branch.setCity("Ha Noi");
        branch.setStatus(BranchStatus.ACTIVE);
        branch.setKitchenCapacity(kitchenCapacity);
        branch = branchRepository.save(branch);

        AreaEntity area = new AreaEntity();
        area.setBranchId(branch.getId());
        area.setName("Floor 1");
        area = areaRepository.save(area);
        for (int t = 1; t <= 5; t++) {
            DiningTableEntity table = new DiningTableEntity();
            table.setAreaId(area.getId());
            table.setName("T" + t);
            diningTableRepository.save(table);
        }
        return branch;
    }

    // n-th table (1-based) of a branch created by createBranch
    protected int table(BranchEntity branch, int n) {
        return diningTableRepository.findByBranchId(branch.getId()).get(n - 1).getId();
    }

    protected ResultActions pay(StaffEntity cashier, int orderId, long cash) throws Exception {
        return mockMvc.perform(post("/payments").with(as(cashier)).contentType(MediaType.APPLICATION_JSON)
            .content("{\"orderId\":" + orderId + ",\"cashReceived\":" + cash + "}"));
    }

    protected MenuItemEntity createMenuItem(String name, long price) {
        MenuCategoryEntity category = new MenuCategoryEntity();
        category.setName("Category " + unique());
        category = menuCategoryRepository.save(category);

        MenuItemEntity item = new MenuItemEntity();
        item.setCategoryId(category.getId());
        item.setCode("M" + unique());
        item.setName(name);
        item.setBasePrice(BigDecimal.valueOf(price));
        return menuItemRepository.save(item);
    }

    protected void overrideAtBranch(BranchEntity branch, MenuItemEntity item, Long price, boolean available) {
        BranchMenuItemEntity override = new BranchMenuItemEntity();
        override.setBranchId(branch.getId());
        override.setMenuItemId(item.getId());
        override.setPrice(price != null ? BigDecimal.valueOf(price) : null);
        override.setIsAvailable(available);
        branchMenuItemRepository.save(override);
    }

    protected StaffEntity createStaff(StaffRole role, BranchEntity branch) {
        int n = unique();
        StaffEntity staff = new StaffEntity();
        staff.setBranchId(branch != null ? branch.getId() : null);
        staff.setCode("S" + n);
        staff.setFullName("Staff " + n);
        staff.setUsername("staff" + n);
        staff.setPasswordHash("not-used");
        staff.setRole(role);
        staff.setStatus(StaffStatus.ACTIVE);
        return staffRepository.save(staff);
    }

    // Logs the request in as this staff member, the same way JwtAuthenticationFilter does
    protected static RequestPostProcessor as(StaffEntity staff) {
        return authentication(new UsernamePasswordAuthenticationToken(
            staff.getId(), null, List.of(new SimpleGrantedAuthority("ROLE_" + staff.getRole().name()))));
    }

    protected static String orderJson(BranchEntity branch, String type, Integer tableId, int[]... menuItemAndQty) {
        StringBuilder items = new StringBuilder();
        for (int[] pair : menuItemAndQty) {
            if (items.length() > 0) items.append(',');
            items.append("{\"menuItemId\":").append(pair[0]).append(",\"quantity\":").append(pair[1]).append('}');
        }
        return "{\"branchId\":" + branch.getId() + ",\"orderType\":\"" + type + "\""
            + (tableId != null ? ",\"tableId\":" + tableId : "")
            + ",\"items\":[" + items + "]}";
    }

    protected static int[] dish(MenuItemEntity item, int quantity) {
        return new int[]{item.getId(), quantity};
    }
}
