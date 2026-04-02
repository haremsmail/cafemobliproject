# Real-Time Café Ordering App - Complete Documentation

## 📱 Project Overview

This is a fully functional Android café ordering application built with Java that allows users to:
- Browse a café menu with items (coffee, tea, desserts)
- Add items to cart
- Manage cart and checkout
- Place orders with real-time timestamps
- View order history
- User authentication and profile management

---

## ✅ Requirement Completion Checklist

### 🔹 Core Features
- ✅ User-friendly home screen with café branding
- ✅ Menu screen displaying items with images, prices, descriptions
- ✅ Add-to-cart functionality with Toast notifications
- ✅ Cart screen showing selected items and total price
- ✅ Order confirmation screen with timestamp
- ✅ User profile screen with order history

### 🔹 Technical Requirements

#### Activities (6 total)
1. **SplashActivity** - Splash screen (3-second delay)
   - Location: `com/example/projectyear/SplashActivity.java`
   - Checks login status and redirects appropriately
   
2. **LoginActivity** - User authentication
   - Location: `com/example/projectyear/LoginActivity.java`
   - Email/password validation
   - User creation and login with MD5 hashing
   - SharedPreferences session management

3. **MainActivity** - Home screen (ConstraintLayout)
   - Location: `com/example/projectyear/MainActivity.java`
   - Navigation hub
   - Database initialization with sample menu items
   - Logout functionality

4. **MenuActivity** - Menu display (LinearLayout)
   - Location: `com/example/projectyear/MenuActivity.java`
   - Hosts MenuFragment with RecyclerView
   - Category filtering support

5. **CartActivity** - Cart management (ConstraintLayout)
   - Location: `com/example/projectyear/CartActivity.java`
   - Displays cart items with quantities
   - Shows subtotal, tax, and total
   - Update/remove item functionality

6. **OrderConfirmationActivity** - Order confirmation (LinearLayout)
   - Location: `com/example/projectyear/OrderConfirmationActivity.java`
   - Saves order to database with timestamp
   - Displays order ID, time, status, and total

7. **ProfileActivity** - User profile
   - Location: `com/example/projectyear/ProfileActivity.java`
   - Shows user email
   - Displays order history in RecyclerView

#### Fragments (1 total)
- **MenuFragment** - RecyclerView for menu items
  - Location: `com/example/projectyear/fragments/MenuFragment.java`
  - Dynamic menu item loading from database
  - Category filtering support

#### Layouts (8+ different types)

**LinearLayouts:**
- `activity_splash.xml` - Splash screen layout
- `activity_login.xml` - Login form
- `activity_order_confirmation.xml` - Order confirmation details

**ConstraintLayouts:**
- `activity_cart.xml` - Cart with summary section
- `activity_main.xml` - Main home screen

**RecyclerView Layouts:**
- `item_menu.xml` - Menu item card (CardView)
- `item_cart_summary.xml` - Cart item display
- `item_order_history.xml` - Order history item
- `fragment_menu.xml` - Fragment container

**Other:**
- `activity_menu.xml` - Menu activity
- `activity_profile.xml` - Profile screen

#### Database (Room ORM)
**Entities:**
- `User.java` - User account info
- `MenuItem.java` - Café menu items
- `Order.java` - Customer orders with timestamp
- `OrderItem.java` - Junction table (Order ↔ MenuItem)

**DAOs:**
- `UserDao.java` - User queries
- `MenuDao.java` - Menu item queries
- `OrderDao.java` - Order queries

**Database Class:**
- `CafeDatabase.java` - Room database singleton

#### Models & Helpers
- `Cart.java` - Session cart management (static)
- `MenuItemAdapter.java` - RecyclerView adapter for menu items
- `CartItemAdapter.java` - RecyclerView adapter for cart items
- `OrderHistoryAdapter.java` - RecyclerView adapter for order history

#### Resources
- `colors.xml` - Café-themed colors (brown/cream palette)
- `strings.xml` - All UI text labels and messages
- `dimens.xml` - Spacing, text sizes, dimensions
- `themes.xml` - Material 3 theme with café colors

---

## 🎨 UI Design & Styling

### Color Scheme (Café Theme)
- **Primary Brown**: `#8B4513`
- **Cream**: `#F5DEB3`
- **Dark Brown**: `#D2691E`
- **Light Brown**: `#A0522D`
- **Beige Background**: `#F5F5DC`

### Layout Strategy
- **Linear Layout** for simple vertical/horizontal arrangements
- **Constraint Layout** for complex responsive designs
- **RecyclerView** with CardView for menu and order items
- **Fragment** for reusable menu component

---

## 🗄️ Database Schema

### users table
```
id (PK) | email | password_hash | created_at
```

### menu_items table
```
id (PK) | name | description | price | category | imageResource | available
```

### orders table
```
id (PK) | userId (FK) | orderDate | status | totalPrice
```

### order_items table
```
id (PK) | orderId (FK) | menuItemId (FK) | quantity | itemPrice
```

---

## 🔄 Navigation Flow

```
SplashActivity (3 seconds delay)
    ↓ (Checks login status)
    ├→ LoginActivity (if not logged in)
    │   ├→ Email/Password Input
    │   ├→ Login Button → MainActivity
    │   └→ Signup Button → Create Account → MainActivity
    │
    └→ MainActivity (if logged in)
        ├→ Browse Menu → MenuActivity
        │   ├→ MenuFragment with RecyclerView
        │   ├→ Add to Cart → Notifications
        │   └→ Go to Cart → CartActivity
        │       ├→ View Items with Quantities
        │       ├→ Continue Shopping → MenuActivity
        │       └→ Checkout → OrderConfirmationActivity
        │           ├→ Save Order to Database
        │           ├→ Show Order ID & Timestamp
        │           └→ Back to Home → MainActivity
        │
        ├→ My Orders → ProfileActivity
        │   ├→ User Email Display
        │   └→ Order History RecyclerView
        │       └→ Shows Order ID, Date, Total
        │
        ├→ My Profile → ProfileActivity
        │   └→ Same as above
        │
        └→ Logout → SharedPreferences cleared → LoginActivity
```

---

## 📦 Sample Menu Items (Auto-populated)

### Coffee (6 items)
- Espresso - Rs. 150
- Latte - Rs. 180
- Cappuccino - Rs. 200
- Americano - Rs. 160
- Mocha - Rs. 220
- Macchiato - (Can be added)

### Tea (3 items)
- Green Tea - Rs. 120
- Black Tea - Rs. 130
- Chamomile - Rs. 140

### Desserts (3 items)
- Chocolate Brownie - Rs. 250
- Croissant - Rs. 180
- Cheesecake - Rs. 300

---

## 🔐 Authentication

**Login Process:**
1. User enters email and password
2. Password hashed using MD5
3. Query database for user with email
4. Compare hashed passwords
5. If match: Save userId in SharedPreferences
6. If no match: Show error toast

**Signup Process:**
1. User enters email and password
2. Validate email format
3. Check if email already exists
4. If new: Hash password and insert new user
5. Save userId in SharedPreferences
6. Navigate to MainActivity

---

## 🛒 Cart Management

**Cart.java** (Static Session Cart):
- `addItem(menuItemId, name, price)` - Add or increment
- `removeItem(menuItemId)` - Remove item
- `updateQuantity(menuItemId, qty)` - Update quantity
- `getSubtotal()` - Calculate subtotal
- `getTax()` - Calculate 5% tax
- `getTotal()` - Subtotal + tax
- `clear()` - Empty cart for new order

---

## 📊 Order Management

**Order Creation:**
1. User clicks "Checkout" in CartActivity
2. OrderConfirmationActivity saves order to database
3. Order record created with current timestamp
4. OrderItems created linking order to menu items
5. Order displayed with confirmation details

**Order History:**
1. ProfileActivity queries user's orders from database
2. Displays order history in RecyclerView
3. Shows Order ID, Date, Total, Status

---

## 🚀 How to Run

### Prerequisites
- Android Studio (Latest)
- JDK 11+
- Android SDK 24+

### Build Steps
1. Open project in Android Studio
2. Sync Gradle files
3. Click Build → Make Project
4. Wait for build to complete
5. Run app on emulator or device

### First Time Usage
1. App shows splash screen (3 seconds)
2. Opens LoginActivity
3. Create account with email and password
4. Browse menu, add items to cart
5. Checkout to place order
6. View orders in profile

---

## 🔧 Key Features Implementation

### Toast Notifications
- "Item added to cart!"
- "Quantity updated"
- "Item removed from cart"
- "Login successful"
- "Account created successfully"

### Real-Time Ordering
- OrderDate stored as timestamp (System.currentTimeMillis())
- Formatted as "dd MMM yyyy, HH:mm"
- Stored in Order entity with order

### User Session
- SharedPreferences stores userId
- Checked on app startup
- Cleared on logout

### Database Threading
- All database operations on background threads
- UI updates on main thread using runOnUiThread()
- Prevents ANR (Application Not Responding)

---

## 📂 Project Structure

```
projectyear/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/projectyear/
│   │   │   ├── SplashActivity.java
│   │   │   ├── LoginActivity.java
│   │   │   ├── MainActivity.java
│   │   │   ├── MenuActivity.java
│   │   │   ├── CartActivity.java
│   │   │   ├── OrderConfirmationActivity.java
│   │   │   ├── ProfileActivity.java
│   │   │   ├── database/
│   │   │   │   ├── User.java
│   │   │   │   ├── MenuItem.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderItem.java
│   │   │   │   ├── UserDao.java
│   │   │   │   ├── MenuDao.java
│   │   │   │   ├── OrderDao.java
│   │   │   │   └── CafeDatabase.java
│   │   │   ├── fragments/
│   │   │   │   └── MenuFragment.java
│   │   │   ├── adapters/
│   │   │   │   ├── MenuItemAdapter.java
│   │   │   │   ├── CartItemAdapter.java
│   │   │   │   └── OrderHistoryAdapter.java
│   │   │   └── models/
│   │   │       └── Cart.java
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_splash.xml
│   │   │   │   ├── activity_login.xml
│   │   │   │   ├── activity_main.xml
│   │   │   │   ├── activity_menu.xml
│   │   │   │   ├── activity_cart.xml
│   │   │   │   ├── activity_order_confirmation.xml
│   │   │   │   ├── activity_profile.xml
│   │   │   │   ├── fragment_menu.xml
│   │   │   │   ├── item_menu.xml
│   │   │   │   ├── item_cart_summary.xml
│   │   │   │   └── item_order_history.xml
│   │   │   ├── values/
│   │   │   │   ├── colors.xml
│   │   │   │   ├── strings.xml
│   │   │   │   ├── dimens.xml
│   │   │   │   └── themes.xml
│   │   │   └── mipmap-*/
│   │   │       └── ic_launcher*.webp
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
├── settings.gradle.kts
├── build.gradle.kts
└── gradlew.bat
```

---

## 🎯 Testing Checklist

- [ ] Login with new account
- [ ] Browse menu items
- [ ] Add multiple items to cart
- [ ] Update item quantities
- [ ] Remove items from cart
- [ ] Calculate total with tax (5%)
- [ ] Place order and see confirmation
- [ ] Verify order saved in database
- [ ] Check order history in profile
- [ ] Logout and login again
- [ ] Previous orders visible in history

---

## 📝 Code Quality

- Clean, well-commented Java code
- Follows Android best practices
- Proper activity lifecycle management
- Database operations on background threads
- UI updates on main thread
- Null safety checks
- Resource management
- Responsive layout design

---

## 🐛 Known Limitations

- Image resources use launcher icons (demo purposes)
- MD5 password hashing (use bcrypt in production)
- No network connectivity (local database only)
- Session expires on app close (can add persistent login)

---

## 🚀 Future Enhancements

1. Replace MD5 with bcrypt for password security
2. Add custom café-themed images for menu items
3. Implement push notifications for order status
4. Add payment integration
5. Support for multiple user addresses
6. Order tracking with real-time updates
7. Ratings and reviews system
8. Favorite items bookmarking
9. Dark mode support
10. Multi-language support

---

## 📞 Support

For issues or questions about the implementation, review:
- Activity documentation comments
- Database schema in entities
- Layout files for UI structure
- Adapter implementations for RecyclerView

---

**Last Updated:** April 1, 2026
**Version:** 1.0
**Status:** Complete and Ready for Use

