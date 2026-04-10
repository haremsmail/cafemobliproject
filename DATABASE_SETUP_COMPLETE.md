# SQLite Database Setup - Complete Working Solution

## Overview
The project now uses **SQLite with Room Database** to store and display menu items with the following structure:
- ☕ **8 Coffee Items** (IQD 1500-2500)
- 🍵 **3 Tea Items** (IQD 1500)
- 🍰 **3 Dessert Items** (IQD 1200-1800)

**Total: 14 Menu Items**

---

## Database Architecture

### 1. **Entity Classes** (Data Models)
Located in: `app/src/main/java/com/example/projectyear/database/`

#### MenuItem.java
- **Table**: `menu_items`
- **Columns**:
  - `id` (Primary Key, Auto-increment)
  - `name` (String)
  - `description` (String)
  - `price` (Double)
  - `category` (String: "Coffee", "Tea", "Desserts")
  - `imageResource` (Int)
  - `available` (Boolean)

#### Other Entities
- **User.java**: Stores user login information
- **Order.java**: Stores customer orders
- **OrderItem.java**: Links orders to menu items with quantities

### 2. **Data Access Objects (DAOs)**
- **MenuDao.java**: Queries for menu items with category filtering
- **UserDao.java**: User authentication queries
- **OrderDao.java**: Order management queries

### 3. **Database Class**
- **CafeDatabase.java**: Room database singleton
  - Tables: users, menu_items, orders, order_items
  - Version: 1
  - Destruc­tive migration enabled for development

### 4. **Database Helper** (NEW)
- **DatabaseHelper.java**: 
  - Initializes database with seed data
  - Thread-safe singleton pattern
  - Methods:
    - `initializeDatabase()`: Initializes and seeds the database
    - `seedMenuItems()`: Inserts 14 menu items
    - `reseedDatabase()`: Clears and reseeds database
    - `clearMenuItems()`: Removes all menu items
    - `getMenuItemCount()`: Returns total menu items

---

## Menu Items Database

### ☕ COFFEE ITEMS (8 items)
```
1. Americano - Smooth espresso diluted with water - IQD 1500
2. Café Latte - Espresso with creamy steamed milk - IQD 2000
3. Cappuccino - Equal parts espresso, milk & foam - IQD 2000
4. Espresso - Rich, bold shot of pure coffee - IQD 1500
5. Flat White - Velvety micro-foam over espresso - IQD 2500
6. Iced Coffee - Cold brewed coffee over ice - IQD 2500
7. Macchiato - Espresso with a dash of foam - IQD 1800
8. Mocha - Chocolate-infused espresso with milk - IQD 2300
```

### 🍵 TEA ITEMS (3 items)
```
9. Black Tea - Classic bold Ceylon black tea - IQD 1500
10. Chamomile - Soothing floral herbal infusion - IQD 1500
11. Green Tea - Delicate, antioxidant-rich green tea - IQD 1500
```

### 🍰 DESSERT ITEMS (3 items)
```
12. Butter Croissant - Flaky golden French croissant - IQD 1200
13. Cheesecake - Creamy New York-style cheesecake - IQD 1800
14. Chocolate Brownie - Warm fudgy brownie with nuts - IQD 1600
```

---

## How It Works

### 1. **Initialization Flow**
```
App Launch
  ↓
MainActivity / Fragments created
  ↓
DatabaseHelper.initializeDatabase() called
  ↓
Check if menu items exist in database
  ↓
If empty → Insert all 14 menu items (seed data)
  ↓
MenuViewModel loads items from database
  ↓
Display in UI (Home, Menu, Featured)
```

### 2. **Data Loading in UI**

#### HomeFragment
- Initializes database
- Loads all menu items
- Displays first 6 items as "Featured Drinks"
- Shows category buttons (Coffee, Tea, Desserts)

#### MenuFragment
- Initializes database
- Displays all menu items in 2-column grid
- Allows filtering by category using chips
- Shows "Add to Cart" buttons

### 3. **Database Operations**

#### Querying Items
```java
// Get all items
List<MenuItem> items = db.menuDao().getAllMenuItems();

// Get by category
List<MenuItem> coffeeItems = db.menuDao().getMenuItemsByCategory("Coffee");

// Get by ID
MenuItem item = db.menuDao().getMenuItemById(itemId);
```

#### Adding Items
```java
MenuItem newItem = new MenuItem("Name", "Description", 1500.0, "Coffee", 0);
db.menuDao().insertMenuItem(newItem);
```

---

## Key Features

✅ **Thread-Safe**: Uses synchronized blocks and single-threaded executor
✅ **Auto-Seeding**: Database is automatically populated on first run
✅ **Category Filtering**: View items by Coffee, Tea, or Desserts
✅ **Proper Pricing**: All prices in IQD (Iraqi Dinar)
✅ **Logging**: Detailed database operations logging
✅ **Development Utilities**: Reseed and clear functions for testing

---

## Testing the Database

### View Database Content
1. Open Android Studio
2. Go to: View → Tool Windows → Device File Explorer
3. Navigate to: `/data/data/com.example.projectyear/databases/cafe_database`
4. Right-click and pull to download
5. Open with SQLite Browser to view tables

### Force Reseed (if needed)
In MenuViewModel:
```java
menuViewModel.reseedDatabase();  // Clears and reseeds all data
menuViewModel.clearMenuItems();  // Clears all items
```

### Check Item Count
```java
int count = DatabaseHelper.getMenuItemCount(context);
```

---

## Application Screens

### Home Screen
- Greeting based on time of day
- User name (from login email)
- Category browse buttons
- Featured drinks horizontal scroll
- Bottom navigation bar

### Menu Screen
- Category filter chips (All, Coffee, Tea, Desserts)
- Grid layout (2 columns)
- Menu items with:
  - Name
  - Description
  - Price (IQD format)
  - "+ Add" button
  - "Added ✓" when in cart

### Cart Screen
- Added items with quantities
- Total price calculation
- Checkout button

### Profile Screen
- User account information
- Order history
- Account settings

---

## File Locations

```
app/src/main/java/com/example/projectyear/
├── database/
│   ├── CafeDatabase.java (Main DB class)
│   ├── DatabaseHelper.java (NEW - DB initialization)
│   ├── MenuItem.java (Entity)
│   ├── MenuDao.java (DAO with query methods)
│   ├── User.java (Entity)
│   ├── Order.java (Entity)
│   └── OrderItem.java (Entity)
├── viewmodels/
│   └── MenuViewModel.java (Loads data from DB)
└── fragments/
    ├── HomeFragment.java (Initializes DB)
    ├── MenuFragment.java (Initializes DB)
    └── ...
```

---

## Dependencies Used

From `build.gradle.kts`:
```kotlin
implementation(libs.room.runtime)      // Room database
annotationProcessor(libs.room.compiler) // Room compiler
implementation(libs.lifecycle.viewmodel) // ViewModel
implementation(libs.lifecycle.runtime)   // Lifecycle
```

---

## Troubleshooting

### Issue: Menu items not showing
**Solution**: 
1. Ensure DatabaseHelper.initializeDatabase() is called
2. Check logcat for database initialization messages
3. Try reseed database: `menuViewModel.reseedDatabase()`

### Issue: Category filter not working
**Solution**: 
- Verify all items have correct category names: "Coffee", "Tea", "Desserts"
- Check MenuDao.getMenuItemsByCategory() query

### Issue: Items in cart not displaying
**Solution**: 
- Check Cart model in models/Cart.java
- Verify CartItemAdapter is bound to CartFragment

### Issue: Prices showing incorrectly
**Solution**: 
- Ensure MenuItem constructor receives Double type
- Check price formatting in adapters: `String.format("IQD %.0f", price)`

---

## Database Schema

```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT NOT NULL,
    passwordHash TEXT NOT NULL,
    createdAt INTEGER NOT NULL
);

CREATE TABLE menu_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT,
    price REAL NOT NULL,
    category TEXT NOT NULL,
    imageResource INTEGER,
    available INTEGER DEFAULT 1
);

CREATE TABLE orders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    userId INTEGER NOT NULL,
    orderDate INTEGER NOT NULL,
    status TEXT,
    totalPrice REAL,
    FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE order_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    orderId INTEGER NOT NULL,
    menuItemId INTEGER NOT NULL,
    quantity INTEGER,
    itemPrice REAL,
    FOREIGN KEY (orderId) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (menuItemId) REFERENCES menu_items(id) ON DELETE CASCADE
);
```

---

## Next Steps

1. ✅ Build and run the app
2. ✅ Navigate to Home screen - should show featured items
3. ✅ Navigate to Menu screen - should show all items filtered by category
4. ✅ Add items to cart
5. ✅ View cart and proceed to checkout
6. ✅ Check order history in Profile

---

## Summary

Your SQLite database is now fully configured with:
- ✅ DatabaseHelper for initialization
- ✅ 14 menu items (Coffee, Tea, Desserts)
- ✅ Proper pricing (IQD)
- ✅ Category filtering
- ✅ Thread-safe operations
- ✅ Proper SQLite Room integration
- ✅ All DAOs and entities properly set up

