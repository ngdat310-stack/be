package com.dinechain.configuration;

import com.dinechain.constant.BranchStatus;
import com.dinechain.constant.DiscountType;
import com.dinechain.constant.StaffRole;
import com.dinechain.constant.StaffStatus;
import com.dinechain.entity.AreaEntity;
import com.dinechain.entity.BranchEntity;
import com.dinechain.entity.BranchInventoryEntity;
import com.dinechain.entity.DiningTableEntity;
import com.dinechain.entity.IngredientEntity;
import com.dinechain.entity.BranchMenuItemEntity;
import com.dinechain.entity.CustomerEntity;
import com.dinechain.entity.MenuCategoryEntity;
import com.dinechain.entity.MenuItemEntity;
import com.dinechain.entity.PromotionBranchEntity;
import com.dinechain.entity.PromotionEntity;
import com.dinechain.entity.RecipeEntity;
import com.dinechain.entity.ShiftEntity;
import com.dinechain.entity.StaffEntity;
import com.dinechain.entity.SupplierEntity;
import com.dinechain.repository.AreaRepository;
import com.dinechain.repository.BranchInventoryRepository;
import com.dinechain.repository.BranchMenuItemRepository;
import com.dinechain.repository.BranchRepository;
import com.dinechain.repository.CustomerRepository;
import com.dinechain.repository.DiningTableRepository;
import com.dinechain.repository.IngredientRepository;
import com.dinechain.repository.MenuCategoryRepository;
import com.dinechain.repository.MenuItemRepository;
import com.dinechain.repository.PromotionBranchRepository;
import com.dinechain.repository.PromotionRepository;
import com.dinechain.repository.RecipeRepository;
import com.dinechain.repository.ShiftRepository;
import com.dinechain.repository.StaffRepository;
import com.dinechain.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo chain of 9 restaurants (menu, 20 tables each, ingredients, recipes, stock, shifts, suppliers, promotions
 * KHAITRUONG and CN01GIAM20K, regular customer 0909000001 with 50 points) so the API can be tried
 * right away (Postman, Swagger).
 * Runs only when dinechain.demo-data.enabled=true and the database has no branch yet.
 *
 * Accounts: owner / Owner@123 (ADMIN, whole chain)
 *           manager1..9, cashier1..9, chef1..9, waiter1..9 / Staff@123 (branch CN01..CN09)
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "dinechain.demo-data.enabled", havingValue = "true")
public class DemoDataSeeder implements ApplicationRunner {

    private static final int BRANCHES = 9;
    private static final String STAFF_PASSWORD = "Staff@123";

    private final BranchRepository branchRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final BranchMenuItemRepository branchMenuItemRepository;
    private final StaffRepository staffRepository;
    private final AreaRepository areaRepository;
    private final DiningTableRepository diningTableRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final BranchInventoryRepository branchInventoryRepository;
    private final SupplierRepository supplierRepository;
    private final ShiftRepository shiftRepository;
    private final PromotionRepository promotionRepository;
    private final PromotionBranchRepository promotionBranchRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (branchRepository.count() > 0) {
            return;
        }

        Map<String, MenuItemEntity> menu = new HashMap<>();
        Integer mains = category("Món chính", 1);
        menu.put("pho", dish(mains, "PHO-BO", "Phở bò", 55000));
        menu.put("buncha", dish(mains, "BUN-CHA", "Bún chả", 50000));
        menu.put("comrang", dish(mains, "COM-RANG", "Cơm rang dưa bò", 45000));
        menu.put("nem", dish(mains, "NEM-RAN", "Nem rán", 40000));
        menu.put("lau", dish(mains, "LAU-THAI", "Lẩu Thái", 250000));
        Integer drinks = category("Đồ uống", 2);
        menu.put("trada", dish(drinks, "TRA-DA", "Trà đá", 5000));
        menu.put("cafe", dish(drinks, "CAFE-SUA", "Cà phê sữa đá", 25000));

        // Ingredients and recipes (quantity per portion); cooking a dish deducts them from the branch stock
        Map<String, IngredientEntity> ingredients = new HashMap<>();
        ingredients.put("banhpho", ingredient("BANH-PHO", "Bánh phở", "kg", "5"));
        ingredients.put("bo", ingredient("THIT-BO", "Thịt bò", "kg", "3"));
        ingredients.put("bun", ingredient("BUN", "Bún", "kg", "5"));
        ingredients.put("heo", ingredient("THIT-HEO", "Thịt heo", "kg", "3"));
        ingredients.put("gao", ingredient("GAO", "Gạo", "kg", "10"));
        ingredients.put("tra", ingredient("TRA", "Trà", "lít", "2"));
        recipe(menu.get("pho"), ingredients.get("banhpho"), "0.2");
        recipe(menu.get("pho"), ingredients.get("bo"), "0.1");
        recipe(menu.get("buncha"), ingredients.get("bun"), "0.2");
        recipe(menu.get("buncha"), ingredients.get("heo"), "0.15");
        recipe(menu.get("comrang"), ingredients.get("gao"), "0.2");
        recipe(menu.get("comrang"), ingredients.get("bo"), "0.08");
        recipe(menu.get("trada"), ingredients.get("tra"), "0.3");

        // Suppliers, promotions and a regular customer with some points
        supplier("Công ty Thực phẩm Sạch Hà Nội", "0243111222", "0101234567");
        supplier("Nông trại Rau Xanh Đà Lạt", "0263999888", "5801234567");
        LocalDateTime now = LocalDateTime.now();
        promotion("KHAITRUONG", "Khai trương giảm 10% (tối đa 50.000)", DiscountType.PERCENT, 10, 50000L, 100000, now, null);
        CustomerEntity regular = new CustomerEntity();
        regular.setPhone("0909000001");
        regular.setFullName("Khách quen Trần Thị B");
        regular.setPoints(50);
        customerRepository.save(regular);

        String passwordHash = passwordEncoder.encode(STAFF_PASSWORD);
        staff("OWNER", "owner", "Chủ chuỗi", passwordEncoder.encode("Owner@123"), StaffRole.ADMIN, null);
        for (int i = 1; i <= BRANCHES; i++) {
            BranchEntity branch = new BranchEntity();
            branch.setCode("CN%02d".formatted(i));
            branch.setName("DineChain chi nhánh " + i);
            branch.setAddress(i + " Phố Huế");
            branch.setCity("Hà Nội");
            branch.setPhone("02439" + "%05d".formatted(i));
            branch.setStatus(BranchStatus.ACTIVE);
            branch = branchRepository.save(branch);

            staff("QL" + i, "manager" + i, "Quản lý CN" + i, passwordHash, StaffRole.BRANCH_MANAGER, branch);
            staff("TN" + i, "cashier" + i, "Thu ngân CN" + i, passwordHash, StaffRole.CASHIER, branch);
            staff("DB" + i, "chef" + i, "Đầu bếp CN" + i, passwordHash, StaffRole.CHEF, branch);
            staff("PV" + i, "waiter" + i, "Phục vụ CN" + i, passwordHash, StaffRole.WAITER, branch);

            // Floor plan: 2 areas x 10 tables
            for (String areaName : new String[]{"Tầng 1", "Tầng 2"}) {
                AreaEntity area = new AreaEntity();
                area.setBranchId(branch.getId());
                area.setName(areaName);
                area = areaRepository.save(area);
                for (int t = 1; t <= 10; t++) {
                    DiningTableEntity table = new DiningTableEntity();
                    table.setAreaId(area.getId());
                    table.setName((areaName.endsWith("1") ? "A" : "B") + "%02d".formatted(t));
                    table.setCapacity(t <= 6 ? 4 : 8);
                    diningTableRepository.save(table);
                }
            }

            // Stock: CN03 is almost out of beef to show the low-stock report
            for (Map.Entry<String, IngredientEntity> ingredient : ingredients.entrySet()) {
                BranchInventoryEntity stock = new BranchInventoryEntity();
                stock.setBranchId(branch.getId());
                stock.setIngredientId(ingredient.getValue().getId());
                stock.setQuantity(i == 3 && ingredient.getKey().equals("bo") ? new BigDecimal("0.5") : new BigDecimal("50"));
                branchInventoryRepository.save(stock);
            }

            // Two shifts a day
            shift(branch, "Ca sáng", LocalTime.of(7, 0), LocalTime.of(15, 0));
            shift(branch, "Ca tối", LocalTime.of(15, 0), LocalTime.of(23, 0));
            if (i == 1) {
                promotion("CN01GIAM20K", "CN01 giảm 20.000", DiscountType.FIXED_AMOUNT, 20000, null, 0, now, branch);
            }

            // Branch-specific menu: CN01 sells pho at a higher price, CN02 ran out of hot pot
            if (i == 1) branchMenu(branch, menu.get("pho"), 60000L, true);
            if (i == 2) branchMenu(branch, menu.get("lau"), null, false);
        }
        log.info("Demo data created: {} branches, {} dishes, owner + {} staff accounts", BRANCHES, menu.size(), BRANCHES * 4);
    }

    private Integer category(String name, int sortOrder) {
        MenuCategoryEntity category = new MenuCategoryEntity();
        category.setName(name);
        category.setSortOrder(sortOrder);
        return menuCategoryRepository.save(category).getId();
    }

    private MenuItemEntity dish(Integer categoryId, String code, String name, long price) {
        MenuItemEntity item = new MenuItemEntity();
        item.setCategoryId(categoryId);
        item.setCode(code);
        item.setName(name);
        item.setBasePrice(BigDecimal.valueOf(price));
        return menuItemRepository.save(item);
    }

    private IngredientEntity ingredient(String code, String name, String unit, String minStock) {
        IngredientEntity ingredient = new IngredientEntity();
        ingredient.setCode(code);
        ingredient.setName(name);
        ingredient.setUnit(unit);
        ingredient.setMinStock(new BigDecimal(minStock));
        return ingredientRepository.save(ingredient);
    }

    private void recipe(MenuItemEntity dish, IngredientEntity ingredient, String quantityPerPortion) {
        RecipeEntity recipe = new RecipeEntity();
        recipe.setMenuItemId(dish.getId());
        recipe.setIngredientId(ingredient.getId());
        recipe.setQuantity(new BigDecimal(quantityPerPortion));
        recipeRepository.save(recipe);
    }

    private void supplier(String name, String phone, String taxCode) {
        SupplierEntity supplier = new SupplierEntity();
        supplier.setName(name);
        supplier.setPhone(phone);
        supplier.setTaxCode(taxCode);
        supplierRepository.save(supplier);
    }

    private void shift(BranchEntity branch, String name, LocalTime start, LocalTime end) {
        ShiftEntity shift = new ShiftEntity();
        shift.setBranchId(branch.getId());
        shift.setName(name);
        shift.setStartTime(start);
        shift.setEndTime(end);
        shiftRepository.save(shift);
    }

    // Runs for a year from now; onlyAt == null means every branch
    private void promotion(String code, String name, DiscountType type, long value, Long maxDiscount, long minOrder,
                           LocalDateTime now, BranchEntity onlyAt) {
        PromotionEntity promotion = new PromotionEntity();
        promotion.setCode(code);
        promotion.setName(name);
        promotion.setDiscountType(type);
        promotion.setDiscountValue(BigDecimal.valueOf(value));
        promotion.setMaxDiscount(maxDiscount != null ? BigDecimal.valueOf(maxDiscount) : null);
        promotion.setMinOrderAmount(BigDecimal.valueOf(minOrder));
        promotion.setStartAt(now.minusDays(1));
        promotion.setEndAt(now.plusYears(1));
        promotion.setUsedCount(0);
        promotion.setIsActive(true);
        promotion = promotionRepository.save(promotion);
        if (onlyAt != null) {
            PromotionBranchEntity link = new PromotionBranchEntity();
            link.setPromotionId(promotion.getId());
            link.setBranchId(onlyAt.getId());
            promotionBranchRepository.save(link);
        }
    }

    private void branchMenu(BranchEntity branch, MenuItemEntity item, Long price, boolean available) {
        BranchMenuItemEntity entry = new BranchMenuItemEntity();
        entry.setBranchId(branch.getId());
        entry.setMenuItemId(item.getId());
        entry.setPrice(price != null ? BigDecimal.valueOf(price) : null);
        entry.setIsAvailable(available);
        branchMenuItemRepository.save(entry);
    }

    private void staff(String code, String username, String fullName, String passwordHash, StaffRole role, BranchEntity branch) {
        StaffEntity staff = new StaffEntity();
        staff.setCode(code);
        staff.setUsername(username);
        staff.setFullName(fullName);
        staff.setPasswordHash(passwordHash);
        staff.setRole(role);
        staff.setStatus(StaffStatus.ACTIVE);
        staff.setBranchId(branch != null ? branch.getId() : null);
        staffRepository.save(staff);
    }
}
