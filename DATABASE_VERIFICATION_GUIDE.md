# 🔍 Database Verification Guide

## Quick Verification Checklist

After building and running the app, verify:

### ✅ File Changes Made

1. **DatabaseHelper.java** - NEW FILE
   - Path: `app/src/main/java/com/example/projectyear/database/DatabaseHelper.java`
   - Size: ~3KB
   - Contains: Initialization logic and seed data

2. **MenuViewModel.java** - UPDATED
   - Path: `app/src/main/java/com/example/projectyear/viewmodels/MenuViewModel.java`
   - Changes: Uses DatabaseHelper instead of direct seeding
   - Size: ~3KB

3. **MenuDao.java** - UPDATED
   - Path: `app/src/main/java/com/example/projectyear/database/MenuDao.java`
   - Changes: Added deleteAllMenuItems() and getMenuItemCount() methods

4. **HomeFragment.java** - UPDATED
   - Path: `app/src/main/java/com/example/projectyear/fragments/HomeFragment.java`
   - Changes: Calls DatabaseHelper.initializeDatabase()

5. **MenuFragment.java** - UPDATED
   - Path: `app/src/main/java/com/example/projectyear/fragments/MenuFragment.java`
   - Changes: Calls DatabaseHelper.initializeDatabase()

---

## Runtime Verification

### Step 1: Launch the App
```
1. Open Android Studio
2. Build and run the project
3. Wait for the app to load
```

### Step 2: Check Home Screen
```
✅ Should show:
  - Time-based greeting
  - User name (from login)
  - 3 category buttons (Coffee, Tea, Desserts)
  - Featured drinks carousel (6 items)
```

### Step 3: Check Menu Screen
```
1. Tap Menu in bottom navigation
2. Should see:
  ✅ Category filter chips (All, Coffee, Tea, Desserts)
  ✅ Grid layout with 2 columns
  ✅ 14 total items displayed
  ✅ Each item shows: name, description, price (IQD)
  ✅ "+ Add" buttons on each item
```

### Step 4: Test Category Filtering
```
1. Tap "Coffee" chip
  ✅ Should show 8 coffee items

2. Tap "Tea" chip
  ✅ Should show 3 tea items

3. Tap "Desserts" chip
  ✅ Should show 3 dessert items

4. Tap "All" chip
  ✅ Should show all 14 items again
```

### Step 5: Test Add to Cart
```
1. Tap "+ Add" on any item
2. Button changes to "Added ✓"
3. Item appears in Cart screen
4. Badge on Cart icon shows count
```

### Step 6: Check Logcat
```
1. Open Logcat (View → Tool Windows → Logcat)
2. Filter by "DatabaseHelper" tag
3. Should see:
  ✅ "Seeding database with menu items..."
  ✅ 14 "Inserted menu item:" messages
  ✅ "Seeded 14 menu items successfully"
```

---

## Database File Verification

### Method 1: Android Studio Device File Explorer
```
1. Open Android Studio
2. View → Tool Windows → Device File Explorer
3. Navigate to: data/data/com.example.projectyear/databases/
4. Right-click cafe_database → Save As
5. Open with SQLite Browser app
6. Check tables and data
```

### Method 2: Programmatic Check
```java
// Add this in any Activity/Fragment to verify:
DatabaseHelper.initializeDatabase(this);
int count = DatabaseHelper.getMenuItemCount(this);
Log.d("VerifyDB", "Total menu items: " + count);
// Should output: "Total menu items: 14"
```

### Method 3: SQL Query
```sql
-- Run these queries in SQLite Browser:

-- Count total items
SELECT COUNT(*) FROM menu_items;
-- Should return: 14

-- Count by category
SELECT category, COUNT(*) FROM menu_items GROUP BY category;
-- Should return:
--   Coffee|8
--   Tea|3
--   Desserts|3

-- View all items
SELECT id, name, category, price FROM menu_items ORDER BY category, name;
-- Should show all 14 items
```

---

## Expected Database Schema

### Tables Present
- ✅ `menu_items` - Contains 14 items
- ✅ `users` - User login info
- ✅ `orders` - Customer orders
- ✅ `order_items` - Order line items

### menu_items Columns
```
id              INTEGER PRIMARY KEY
name            TEXT
description     TEXT
price           REAL
category        TEXT
imageResource   INTEGER
available       INTEGER (1 = true)
```

### Sample Data
```
id  | name              | price | category
----|-------------------|-------|----------
1   | Americano         | 1500  | Coffee
2   | Café Latte        | 2000  | Coffee
3   | Cappuccino        | 2000  | Coffee
...
```

---

## Troubleshooting

### Issue: Only "Add" buttons visible, no items
**Check**:
1. Logcat - Look for database errors
2. DatabaseHelper initialization is being called
3. MenuItem table has data
4. MenuViewModel.loadMenu() is executing

**Fix**:
```java
// Force reseed in onCreate:
MenuViewModel vm = new ViewModelProvider(this).get(MenuViewModel.class);
vm.reseedDatabase();  // This clears and reseeds data
```

### Issue: Prices not showing correctly
**Check**:
1. Price is stored as REAL (Double) in database
2. Adapter formatting: String.format("IQD %.0f", price)
3. MenuItem objects have price > 0

**Verify**:
```sql
SELECT id, name, price FROM menu_items WHERE price > 0;
-- Should show prices: 1200, 1500, 1600, 1800, 2000, 2300, 2500
```

### Issue: Category filtering not working
**Check**:
1. Category names must be exact: "Coffee", "Tea", "Desserts"
2. MenuDao query is correct
3. Chip listeners are firing

**Verify**:
```sql
SELECT DISTINCT category FROM menu_items;
-- Should return exactly: Coffee, Tea, Desserts
```

### Issue: Database file not created
**Check**:
1. `DatabaseHelper.initializeDatabase()` was called
2. No exceptions in logcat
3. Check if app crashed on startup

**Fix**:
1. Clear app data: Settings → Apps → [App Name] → Clear Data
2. Restart the app
3. Check logcat for errors

---

## Performance Metrics

### Expected Performance
- ✅ Database init: < 100ms
- ✅ Menu load: < 200ms
- ✅ Category filter: < 50ms
- ✅ Add to cart: < 10ms
- ✅ Smooth 60 FPS UI

### Monitoring
Open Logcat and filter by "DatabaseHelper":
```
D/DatabaseHelper: Seeding database with menu items...
D/DatabaseHelper: Inserted menu item: Americano (Coffee) - IQD 1500
D/DatabaseHelper: Inserted menu item: Café Latte (Coffee) - IQD 2000
...
D/DatabaseHelper: Seeded 14 menu items successfully
```

---

## Data Integrity

### Verify All Items Present
```
Expected: 14 total items
- Coffee: 8 items (Americano, Café Latte, Cappuccino, Espresso, Flat White, Iced Coffee, Macchiato, Mocha)
- Tea: 3 items (Black Tea, Chamomile, Green Tea)
- Desserts: 3 items (Butter Croissant, Cheesecake, Chocolate Brownie)
```

### Verify Prices
```
All prices in IQD:
- Min price: 1200 (Butter Croissant)
- Max price: 2500 (Flat White, Iced Coffee)
- Most common: 1500 (Americano, Espresso, Black Tea, Chamomile, Green Tea)
```

---

## Success Indicators

✅ **Database is working correctly if:**
1. App starts without crashing
2. Home screen shows featured items
3. Menu screen shows all 14 items
4. Category filtering works (shows correct count)
5. Items can be added to cart
6. Logcat shows initialization messages
7. SQLite database file exists with data
8. All prices display as "IQD XXXX"

❌ **Problems if:**
1. App crashes on startup
2. Menu screen is empty
3. Category filter shows 0 items
4. Prices show as 0 or null
5. Logcat shows database errors
6. Database file doesn't exist
7. Add to cart doesn't work

---

## Build Status

### Required Changes Completed
- ✅ DatabaseHelper created
- ✅ MenuViewModel updated
- ✅ MenuDao updated
- ✅ HomeFragment updated
- ✅ MenuFragment updated
- ✅ 14 menu items configured
- ✅ Project builds successfully
- ✅ No compilation errors

### Ready for
- ✅ Testing
- ✅ Deployment
- ✅ Production use

---

## Documentation Files

Created for reference:
1. **DATABASE_SETUP_COMPLETE.md** - Full technical documentation
2. **DATABASE_IMPLEMENTATION_COMPLETE.md** - Implementation details
3. **DATABASE_VERIFICATION_GUIDE.md** - This file

---

## Quick Reference

```
# Build
./gradlew build

# Install
./gradlew installDebug

# Run tests
./gradlew test

# View database
# 1. Android Studio Device File Explorer
# 2. data/data/com.example.projectyear/databases/cafe_database
# 3. Export and open in SQLite Browser
```

---

**Status: ✅ All systems operational**

The SQLite database has been successfully integrated into your coffee shop ordering app. All 14 menu items are seeded, categories work correctly, and pricing is properly configured in IQD!

