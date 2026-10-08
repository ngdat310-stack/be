package com.dinechain.security;

/**
 * Who may call what, used with @PreAuthorize. Branch-level checks (staff only touch their own branch)
 * are done on top of this in the services via BranchAccessGuard.
 */
public final class Roles {

    // Chain owner only: branches, chain menu, chain dashboard
    public static final String ADMIN = "hasRole('ADMIN')";
    // Owner and branch managers: staff, refunds, branch settings
    public static final String MANAGEMENT = "hasAnyRole('ADMIN','BRANCH_MANAGER')";
    // Taking orders and looking after guests
    public static final String FRONT_OF_HOUSE = "hasAnyRole('ADMIN','BRANCH_MANAGER','CASHIER','WAITER')";
    // Handling money and closing orders
    public static final String CASHIER = "hasAnyRole('ADMIN','BRANCH_MANAGER','CASHIER')";
    // Cooking
    public static final String KITCHEN = "hasAnyRole('ADMIN','BRANCH_MANAGER','CHEF')";
    // Everyone working the floor or the kitchen (not the warehouse)
    public static final String SERVICE = "hasAnyRole('ADMIN','BRANCH_MANAGER','CASHIER','WAITER','CHEF')";
    // Changing stock
    public static final String STOCK = "hasAnyRole('ADMIN','BRANCH_MANAGER','WAREHOUSE')";
    // Looking at stock (chefs need to know what is running out)
    public static final String STOCK_READ = "hasAnyRole('ADMIN','BRANCH_MANAGER','WAREHOUSE','CHEF')";

    private Roles() {
    }
}
