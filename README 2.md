Sante-Price Index

SANTE-PRICE INDEX: Complete Standard Operating Procedure (SOP)
Vendor Intelligence & Fair-Price Platform - Android Development Guide

📋 TABLE OF CONTENTS
Executive Summary
Detailed Screen Prompts for AI Agent
Screen Connectivity & Navigation Flow
Backend Architecture & Firebase Configuration
UI/UX Design System
Development Checklist
Testing & Deployment Protocol
Sample Code Snippets
Final Deliverables
Project Success Criteria Summary
1. EXECUTIVE SUMMARY
Project Name: Sante-Price Index (Vendor Intelligence Platform)
Target Platform: Android (Kotlin + Jetpack Compose)
Backend: Firebase Realtime Database + Firestore
Core Purpose: Empower rural vendors with data-driven pricing intelligence to bridge the gap between wholesale Mandi prices and fair retail pricing

Key Metrics:
Target Users: Small-scale street vendors at weekly Sante markets
Core Commodities: Onion, Tomato, Potato, Green Chilli, Garlic
Pricing Algorithm: Cost-Plus with transport, wastage, and profit margin variables
Critical Feature: High-contrast Digital Price Board (Yellow on Black) for outdoor visibility
Problem Being Solved:
Village vendors buying from city wholesale markets (Mandis) lack:

Real-time price visibility
Understanding of hidden costs (transport, wastage)
Tools to calculate fair, profitable retail prices
Transparent pricing displays for customer trust
2. DETAILED SCREEN PROMPTS FOR AI AGENT
🔐 SCREEN 1: AUTHENTICATION & MARKET ONBOARDING
Prompt ID: SCREEN_AUTH_MARKET_001

text

Create a Clean Brutalist authentication and market selection flow:

SPLASH/BRANDING:
- Center a bold 120dp "SP" monogram in Indigo-600 (#4F46E5)
- App name "Sante-Price Index" in Outfit font, 28sp
- Tagline: "Fair Prices, Fair Profits" in Slate-600 (#475569), 14sp
- Background: Soft gradient from Slate-50 to Slate-100

AUTHENTICATION:
- Firebase Google Sign-In button (full width, rounded-3xl)
- Button style: Indigo-600 background, white text, 56dp height
- Icon: Google logo on left side
- Text: "Continue with Google" in Inter font, 16sp semibold

MARKET SELECTION SCREEN:
Layout: Grid of 2 columns, 3 rows
Market Cards: Ultra-rounded (32dp radius), elevation 4dp

Each card shows:
┌──────────────────────────┐
│  📍 City Icon            │
│  BANGALORE               │
│  (Market Name - Large)   │
│  Active Today            │
└──────────────────────────┘

Markets to display:
1. Bangalore (KR Market)
2. Mumbai (Vashi APMC)
3. Chennai (Koyambedu)
4. Pune (Market Yard)
5. Delhi (Azadpur Mandi)

Card interaction:
- OnClick: Save selected market to Firestore user document
- Field: activeMarket: "Bangalore"
- Visual feedback: Selected card shows Indigo-600 border (4dp)

FIRESTORE SAVE OPERATION:
Collection: users
Document ID: user.uid
Data Model:
{
  userId: String,
  email: String,
  activeMarket: String,
  registrationDate: Timestamp,
  lastLogin: Timestamp
}

POST-SELECTION:
Navigate to Price Watch Dashboard (Screen 2)
📊 SCREEN 2: PRICE WATCH (The Live Feed)
Prompt ID: SCREEN_PRICE_WATCH_002

text

Build the main Price Watch dashboard with Clean Brutalist design:

HEADER SECTION:
- Top bar: "Price Watch" title (Outfit, 24sp, bold)
- Right side: Market dropdown selector (current market name + chevron-down icon)
- Last updated timestamp: "Updated 2 mins ago" (Inter, 12sp, Slate-500)

COMMODITY PRICE LIST:
Use LazyColumn with staggered animation (fadeIn + slideInVertically)

Each commodity card (full width, 16dp horizontal padding, 12dp vertical):
┌────────────────────────────────────────────────┐
│  🧅 Onion                        ↗️ TrendingUp  │
│  ₹22.50/kg                                     │
│  Pune Mandi • Fresh Today                     │
└────────────────────────────────────────────────┘

Card specifications:
- Background: White
- Border: 1dp Slate-200
- Corner radius: 24dp
- Padding: 20dp

Card elements:
1. Commodity icon (emoji or vector, 32dp)
2. Name (Inter, 18sp, semibold)
3. Price (Outfit, 28sp, bold, Indigo-600)
4. Unit ("/kg", "/bundle", etc.)
5. Trend icon:
   - TrendingUp (Lucide React icon, 20dp, Green-500) if price rising
   - TrendingDown (Red-500) if falling
   - Minus (Slate-400) if stable
6. Source: "Pune Mandi • Fresh Today" (Inter, 12sp, Slate-500)

Commodity list:
- Onion
- Tomato
- Potato
- Green Chilli
- Garlic

SMART TIP FOOTER:
Fixed bottom container (z-index 10):
┌────────────────────────────────────────────────┐
│  💡 Smart Tip                                  │
│  "Tomato prices down 15% - Good buy today!"   │
└────────────────────────────────────────────────┘

Styling:
- Background: Indigo-600
- Text color: White
- Padding: 16dp
- Corner radius: 24dp top corners only
- Font: Inter, 14sp

FIREBASE REAL-TIME LISTENER:
Collection: prices
Document: {activeMarket}_{today's date}
Query:
  .collection("prices")
  .document("${userMarket}_${getCurrentDate()}")
  .collection("commodities")
  .addSnapshotListener { snapshot, error ->
      // Update UI with real-time price changes
  }

INTERACTIONS:
- Pull to refresh: Manual Firebase fetch
- Tap commodity card: Navigate to Profit Calculator with pre-filled Mandi price
- Tap market dropdown: Show market selector overlay
🧮 SCREEN 3: PROFIT CALCULATOR (Intelligence Suite)
Prompt ID: SCREEN_PROFIT_CALC_003

text

Design the Profit Calculator as the core intelligence tool:

HEADER:
- Title: "Profit Calculator" (Outfit, 24sp, bold)
- Subtitle: "Smart pricing for your business" (Inter, 14sp, Slate-500)

INPUT SECTION (Card with Slate-100 background, 24dp padding):

1. Commodity Selector:
   - Dropdown showing all commodities
   - If navigated from Price Watch: Pre-selected commodity
   - Large text display of commodity name (20sp)

2. Mandi Price Input:
   ┌──────────────────────────────┐
   │  Mandi Price (₹/kg)          │
   │  [    22.50    ]             │
   │  Auto-filled from Price Feed │
   └──────────────────────────────┘
   - TextField: Outline style, 64dp height
   - Keyboard: Number pad with decimal
   - Font: Outfit, 32sp for input
   - Hint: Current market price

3. Transport Cost Input:
   ┌──────────────────────────────┐
   │  Transport Cost (₹/kg)       │
   │  [     3.00     ]            │
   └──────────────────────────────┘
   - Same styling as above
   - Default: 0.00 (user must enter)

4. Wastage Percentage Slider:
   ┌──────────────────────────────┐
   │  Wastage Loss        [15%]   │
   │  ○━━━━━━━━●━━━━━━━━○        │
   │  0%              50%         │
   └──────────────────────────────┘
   - Slider: Indigo-600 active track
   - Range: 0% to 50%
   - Step: 1%
   - Live percentage display above slider (Outfit, 24sp)

5. Profit Margin Slider:
   ┌──────────────────────────────┐
   │  Target Profit      [25%]    │
   │  ○━━━━━━━━━●━━━━━━━━○       │
   │  0%              100%        │
   └──────────────────────────────┘
   - Same styling as wastage slider
   - Range: 0% to 100%
   - Default: 20%

CALCULATION LOGIC (Kotlin):
```kotlin
// Step 1: Landed Cost
val landedCost = mandiPrice + transportCost

// Step 2: Effective Cost (accounting for wastage)
val effectiveCost = landedCost / (1 - (wastagePercent / 100))

// Step 3: Recommended Retail Price
val rrp = effectiveCost * (1 + (profitMargin / 100))

// Step 4: Net Profit per unit
val netProfit = rrp - effectiveCost

// Step 5: Gross Revenue (if batch size entered)
val grossRevenue = rrp * batchSize
RESULT CARD (High-contrast dark card):
┌────────────────────────────────────────────────┐
│ RECOMMENDED PRICE │
│ │
│ ₹ 35.50 │
│ per kg │
│ │
│ ─────────────────────────────────────────── │
│ Cost Breakdown │
│ Mandi Price: ₹22.50 │
│ + Transport: ₹ 3.00 │
│ ───────────────────────── │
│ Landed Cost: ₹25.50 │
│ + Wastage (15%): ₹ 3.82 │
│ ───────────────────────── │
│ Effective Cost: ₹29.32 │
│ + Profit (25%): ₹ 7.33 │
│ ═════════════════════════ │
│ RETAIL PRICE: ₹35.50 │
│ │
│ Your Profit: ₹6.18 per kg │
└────────────────────────────────────────────────┘

Result card styling:

Background: Slate-900 (near black)
Text: Electric Yellow (#FFD600) for prices
Secondary text: Slate-300
RRP: Outfit font, 64sp, bold
Breakdown: Inter font, 16sp
FINANCIAL LITERACY SECTION:
Educational panel (expandable):
┌────────────────────────────────────────────────┐
│ 📚 Understanding Your Profit │
│ │
│ Gross Revenue (100kg): ₹3,550 │
│ Total Cost (100kg): ₹2,932 │
│ ═════════════════════════ │
│ NET PROFIT: ₹618 │
│ Profit Margin: 21.1% │
└────────────────────────────────────────────────┘

ACTION BUTTONS:

"Send to Price Board" (Indigo-600, full width)
Saves RRP to local storage
Navigates to Digital Price Board
"Calculate for Batch" (Outline button)
Opens dialog to enter quantity
Shows total profit
REAL-TIME CALCULATION:

Any input change triggers instant recalculation
Smooth number transition animation
No submit button needed (live updates)
text


---

### **📈 SCREEN 4: TRENDS & AI FORECAST (Gemini Integration)**

**Prompt ID:** `SCREEN_TRENDS_AI_004`
Create a Market Analysis screen with price trends and AI predictions:

HEADER:

Title: "Market Trends" (Outfit, 24sp)
Subtitle: Selected commodity name (Inter, 16sp, Slate-600)
COMMODITY SELECTOR:

Horizontal scrollable chip row
Each chip: Commodity name + icon
Selected chip: Indigo-600 background
Unselected: Slate-200 background
7-DAY PRICE CHART:
Use MPAndroidChart library (or Recharts if React)

Chart specifications:

Type: Line chart
Line color: Indigo-600
Line width: 4dp
Fill gradient: Indigo-600 (top) to transparent (bottom)
Grid: Slate-200 dashed lines
Y-axis: Price in ₹
X-axis: Last 7 days (Mon, Tue, Wed...)
Touch interaction: Show tooltip with exact price
Height: 250dp
Data points (simulated from Firebase):

Kotlin

data class PricePoint(
    val date: String,      // "2025-01-15"
    val price: Float,      // 22.50
    val market: String     // "Pune"
)
TREND INDICATOR:
Below chart, show large trend summary:
┌────────────────────────────────────────────────┐
│ 📊 7-Day Movement │
│ │
│ ↗️ +15.5% │
│ Prices rising steadily │
└────────────────────────────────────────────────┘

Conditional styling:

Rising: Green-500 arrow, Green-50 background
Falling: Red-500 arrow, Red-50 background
Stable: Slate-400 horizontal line, Slate-50 background
AI FORECAST SECTION (Gemini Integration):
┌────────────────────────────────────────────────┐
│ 🤖 AI Forecast │
│ │
│ "Onion prices likely to stabilize in the │
│ next 24 hours. Ideal time to stock up." │
│ │
│ Confidence: High ███████░░ 70% │
└────────────────────────────────────────────────┘

Gemini API Call (Simulated):

Kotlin

fun generateForecast(commodity: String, priceHistory: List<Float>): String {
    val prompt = """
    You are a market analyst for rural Indian vegetable markets.
    Analyze this 7-day price trend for ${commodity}:
    Prices: ${priceHistory.joinToString(", ")}
    
    Provide a one-sentence forecast (max 25 words) advising vendors
    whether to buy stock now or wait. Be simple and actionable.
    """
    
    // Call Gemini API
    val response = geminiClient.generateContent(prompt)
    return response.text
}
PRICE ALERTS (Optional - Good to Have):
Toggle switch:
┌────────────────────────────────────────────────┐
│ 🔔 Notify me when Onion price drops below: │
│ [ ₹20.00 ] per kg │
│ [Enable Alert Toggle ━━━○ ] │
└────────────────────────────────────────────────┘

FIREBASE SCHEMA (Historical Prices):
Collection: price_history
Document: {market}_{commodity}
Data:

JSON

{
  "commodity": "onion",
  "market": "Pune",
  "history": [
    {
      "date": "2025-01-15",
      "price": 22.50,
      "change": +1.2
    },
    // ... last 7 days
  ]
}
text


---

### **💬 SCREEN 5: VENDOR HUB (Community Chat) - OPTIONAL**

**Prompt ID:** `SCREEN_VENDOR_HUB_005`
Implement a community messaging feature for local vendor collaboration:

HEADER:

Title: "Vendor Hub" (Outfit, 24sp)
Subtitle: "Connect with local vendors" (Inter, 14sp)
CHAT SCOPE TOGGLE:
Segmented control at top:
┌────────────────────────────────────────────────┐
│ [ Market Chat ] [ Global Chat ] │
└────────────────────────────────────────────────┘

Market Chat: Filtered by activeMarket
Global Chat: All vendors nationwide
CHAT INTERFACE:
Standard messenger layout:

Message bubbles (sent by user):
┌────────────────────────────────────────────────┐
│ │
│ "Tomato price good today!" │
│ Rajesh • 2:34 PM │
└────────────────────────────────────────────────┘

Alignment: Right
Background: Indigo-600
Text color: White
Max width: 75% of screen
Message bubbles (received):
┌────────────────────────────────────────────────┐
│ "Anyone buying from Kolar?" │
│ Priya • 2:32 PM │
│ │
└────────────────────────────────────────────────┘

Alignment: Left
Background: Slate-100
Text color: Slate-900
INPUT SECTION (Bottom):
┌────────────────────────────────────────────────┐
│ [ Type message... ] [Send →] │
└────────────────────────────────────────────────┘

FIRESTORE SCHEMA:
Collection: vendor_chats
Document: {market_id}
Subcollection: messages
Document: {message_id}
Data:

JSON

{
  "senderId": "user123",
  "senderName": "Rajesh Kumar",
  "message": "Tomato price good today!",
  "timestamp": ServerTimestamp,
  "market": "Pune"  // for filtering
}
FEATURES:

Real-time Firestore snapshot listener
Auto-scroll to latest message
Server-side timestamp ordering
Filter by market in query
text


---

### **🖥️ SCREEN 6: DIGITAL RATE BOARD (The Storefront)**

**Prompt ID:** `SCREEN_DIGITAL_BOARD_006`
Build the full-screen Digital Price Board optimized for outdoor visibility:

CRITICAL REQUIREMENTS:

Maximum outdoor legibility under direct sunlight
Readable from 2+ meters distance
High-contrast color scheme (7:1 ratio minimum)
No transparency or gradient effects
Keep screen awake (WakeLock)
LAYOUT SPECIFICATIONS:

FULL-SCREEN MODE:

Status bar: Hidden (immersive mode)
Navigation bar: Hidden
Orientation: Locked to portrait
Background: Pure Black (#000000)
Text: Electric Yellow (#FFD600)
HEADER (Sticky top):
┌────────────────────────────────────────────────┐
│ TODAY'S PRICES [Exit ✕] │
│ Updated: Jan 15, 2:30 PM │
└────────────────────────────────────────────────┘

Font: Outfit, 20sp, Yellow
Exit button: Top-right, returns to normal mode
PRICE LIST (Center-aligned, vertical scroll):

Each commodity row:
┌────────────────────────────────────────────────┐
│ │
│ ONION │
│ ₹35/kg │
│ │
└────────────────────────────────────────────────┘

Row specifications:

Commodity name: Outfit, 36sp, bold, uppercase
Price: Outfit, 72sp, ultra-bold
Spacing: 48dp between rows
Padding: 32dp horizontal
FONT SIZE CONTROLS (Overlay buttons, semi-transparent):
Bottom right corner:
┌──────────┐
│ [ A⁺ ] │ // Increase font
│ [ A⁻ ] │ // Decrease font
└──────────┘

Range: 48sp to 120sp
Step: 12sp
Persist preference to SharedPreferences
MANDI MODE TOGGLE:
Top-left icon button:
┌────────────────────────────────────────────────┐
│ [👁️ Internal View] │
└────────────────────────────────────────────────┘

When enabled, show dual prices:
┌────────────────────────────────────────────────┐
│ ONION │
│ Buy: ₹22 → Sell: ₹35 │
│ ₹13 profit │
└────────────────────────────────────────────────┘

Styling adjustments:

Buy price: Slate-400 (dimmed)
Sell price: Yellow (bright)
Profit: Green-400
SCREEN BRIGHTNESS:
Auto-set to maximum when Price Board opens:

Kotlin

val layoutParams = window.attributes
layoutParams.screenBrightness = 1.0f  // 100%
window.attributes = layoutParams
WAKE LOCK:
Keep screen on while Price Board is active:

Kotlin

window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
DATA SOURCE:
Pull prices from:

User's saved calculator results (primary)
Firestore price feed (fallback)
Local cache (offline mode)
ACCESSIBILITY:

TalkBack: Read each price aloud
High contrast: Already implemented via Yellow-Black
Touch target: Entire screen tap exits mode
ANDROID IMPLEMENTATION (Compose):

Kotlin

@Composable
fun DigitalPriceBoard(
    prices: List<CommodityPrice>,
    fontSize: Dp,
    mandiMode: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(48.dp),
            contentPadding = PaddingValues(32.dp)
        ) {
            items(prices) { item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = item.name.uppercase(),
                        fontSize = (fontSize * 0.5f).sp,
                        color = Color(0xFFFFD600),
                        fontFamily = OutfitFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "₹${item.retailPrice}/kg",
                        fontSize = fontSize.sp,
                        color = Color(0xFFFFD600),
                        fontFamily = OutfitFontFamily,
                        fontWeight = FontWeight.ExtraBold
                    )
                    
                    if (mandiMode) {
                        Text(
                            text = "Buy: ₹${item.mandiPrice}",
                            fontSize = (fontSize * 0.3f).sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }
        
        // Font controls overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            FloatingActionButton(
                onClick = { /* Increase font */ },
                containerColor = Color(0x80000000)
            ) {
                Text("A⁺", color = Color.White)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            FloatingActionButton(
                onClick = { /* Decrease font */ },
                containerColor = Color(0x80000000)
            ) {
                Text("A⁻", color = Color.White)
            }
        }
    }
}
text


---

## **3. SCREEN CONNECTIVITY & NAVIGATION FLOW**
┌─────────────────────────────────────────────────────────────┐
│ SANTE-PRICE INDEX NAVIGATION MAP │
└─────────────────────────────────────────────────────────────┘

START
│
▼
┌─────────────────┐
│ Screen 1 │
│ AUTH + MARKET │
│ SELECTION │
└────────┬────────┘
│
│ After Google Sign-In + Market Selection
│
▼
┌─────────────────┐
│ Screen 2 │◄───────────────┐
│ PRICE WATCH │ │
│ (Home) │ │
└────────┬────────┘ │
│ │
├──── Tap Commodity Card ──┴─────┐
│ │
│ ▼
│ ┌─────────────────┐
│ │ Screen 3 │
│ │ PROFIT CALC │
│ └────────┬────────┘
│ │
│ │
│ ◄───── "Send to Price Board" ───┤
│ │
▼ ▼
┌─────────────────┐ ┌─────────────────┐
│ Screen 6 │ │ Screen 4 │
│ DIGITAL BOARD │ │ TRENDS & AI │
│ (Fullscreen) │ └─────────────────┘
└─────────────────┘

BOTTOM NAVIGATION BAR (Persistent across screens 2-5):
┌────────────────────────────────────────────────────────────┐
│ [📊 Watch] [🧮 Calculator] [📈 Trends] [🖥️ Board] │
└────────────────────────────────────────────────────────────┘

Icon Mapping:

Watch → Screen 2 (Price Watch)
Calculator → Screen 3 (Profit Calculator)
Trends → Screen 4 (Market Trends)
Board → Screen 6 (Digital Rate Board)
════════════════════════════════════════════════════════════════

DATA FLOW DIAGRAM:

┌─────────────┐
│ Firebase │
│ Realtime │──┬──► Screen 2 (Price Watch)
│ Database │ │
└─────────────┘ │
├──► Screen 4 (Trends - Historical)
│
└──► Screen 6 (Digital Board - Fallback)

┌─────────────┐
│ Local │──┬──► Screen 3 (Calculator State)
│ Calculation │ │
│ Engine │ └──► Screen 6 (Primary Price Source)
└─────────────┘

User Journey Flow:

First-time user: Auth → Market Selection → Price Watch
Returning user: Auto-login → Last selected market → Price Watch
Typical workflow:
Open app → View prices (Screen 2)
Select commodity → Calculate profit (Screen 3)
Send to board → Show customers (Screen 6)
Check trends periodically (Screen 4)
════════════════════════════════════════════════════════════════

ANDROID NAVIGATION IMPLEMENTATION (Jetpack Compose):

Kotlin

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object MarketSelection : Screen("market_selection")
    object PriceWatch : Screen("price_watch")
    object ProfitCalculator : Screen("calculator/{commodityId}") {
        fun createRoute(commodityId: String) = "calculator/$commodityId"
    }
    object Trends : Screen("trends/{commodityId}") {
        fun createRoute(commodityId: String) = "trends/$commodityId"
    }
    object VendorHub : Screen("vendor_hub")
    object DigitalBoard : Screen("digital_board")
}

@Composable
fun SantePriceNavigation() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            // Show only on main screens
            if (currentRoute in listOf("price_watch", "calculator", "trends")) {
                BottomNavigationBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Auth.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Auth.route) {
                AuthScreen(
                    onAuthSuccess = {
                        navController.navigate(Screen.MarketSelection.route)
                    }
                )
            }
            
            composable(Screen.MarketSelection.route) {
                MarketSelectionScreen(
                    onMarketSelected = { market ->
                        // Save to Firestore
                        navController.navigate(Screen.PriceWatch.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Screen.PriceWatch.route) {
                PriceWatchScreen(
                    onCommodityClick = { commodityId ->
                        navController.navigate(
                            Screen.ProfitCalculator.createRoute(commodityId)
                        )
                    }
                )
            }
            
            composable(
                route = Screen.ProfitCalculator.route,
                arguments = listOf(navArgument("commodityId") { type = NavType.StringType })
            ) { backStackEntry ->
                val commodityId = backStackEntry.arguments?.getString("commodityId")
                ProfitCalculatorScreen(
                    commodityId = commodityId,
                    onSendToBoard = { calculatedPrices ->
                        // Save to local storage
                        navController.navigate(Screen.DigitalBoard.route)
                    }
                )
            }
            
            composable(Screen.Trends.route) { backStackEntry ->
                TrendsScreen()
            }
            
            composable(Screen.DigitalBoard.route) {
                DigitalPriceBoardScreen(
                    onExitFullscreen = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == "price_watch",
            onClick = { navController.navigate(Screen.PriceWatch.route) },
            icon = { Icon(Icons.Default.List, "Price Watch") },
            label = { Text("Watch") }
        )
        
        NavigationBarItem(
            selected = currentRoute?.startsWith("calculator") == true,
            onClick = { navController.navigate(Screen.ProfitCalculator.createRoute("default")) },
            icon = { Icon(Icons.Default.Calculate, "Calculator") },
            label = { Text("Calculator") }
        )
        
        NavigationBarItem(
            selected = currentRoute?.startsWith("trends") == true,
            onClick = { navController.navigate(Screen.Trends.createRoute("onion")) },
            icon = { Icon(Icons.Default.TrendingUp, "Trends") },
            label = { Text("Trends") }
        )
        
        NavigationBarItem(
            selected = currentRoute == "digital_board",
            onClick = { navController.navigate(Screen.DigitalBoard.route) },
            icon = { Icon(Icons.Default.Dashboard, "Board") },
            label = { Text("Board") }
        )
    }
}
text


---

## **4. BACKEND ARCHITECTURE & FIREBASE CONFIGURATION**

### **4.1 Firebase Realtime Database Schema**
FIREBASE REALTIME DATABASE STRUCTURE:

markets/
│
├── bangalore/
│ ├── metadata/
│ │ ├── name: "Bangalore - KR Market"
│ │ ├── location: { lat: 12.9716, lng: 77.5946 }
│ │ └── active: true
│ │
│ └── prices/
│ └── 2025-01-15/
│ ├── onion: { price: 22.50, unit: "kg", updated: 1736915400000 }
│ ├── tomato: { price: 18.00, unit: "kg", updated: 1736915400000 }
│ ├── potato: { price: 14.00, unit: "kg", updated: 1736915400000 }
│ ├── green_chilli: { price: 35.00, unit: "kg", updated: 1736915400000 }
│ └── garlic: { price: 80.00, unit: "kg", updated: 1736915400000 }
│
├── mumbai/
│ └── ... (same structure)
│
├── chennai/
├── pune/
└── delhi/

price_history/
│
└── {market}_{commodity}/
└── records/
├── 2025-01-09: { price: 20.00, change: -2.5 }
├── 2025-01-10: { price: 21.00, change: +5.0 }
├── 2025-01-11: { price: 21.50, change: +2.4 }
├── 2025-01-12: { price: 22.00, change: +2.3 }
├── 2025-01-13: { price: 22.00, change: 0.0 }
├── 2025-01-14: { price: 22.50, change: +2.3 }
└── 2025-01-15: { price: 22.50, change: 0.0 }

users/
│
└── {userId}/
├── email: "rajesh@example.com"
├── activeMarket: "bangalore"
├── registrationDate: 1736915400000
├── lastLogin: 1736915400000
└── preferences/
├── language: "en"
├── defaultProfitMargin: 25
└── defaultWastage: 15

vendor_chats/ (Optional)
│
└── {marketId}/
└── messages/
└── {messageId}/
├── senderId: "user123"
├── senderName: "Rajesh Kumar"
├── message: "Good prices today!"
├── timestamp: 1736915400000
└── market: "bangalore"

text


### **4.2 Firebase Security Rules**

```json
{
  "rules": {
    "markets": {
      ".read": "auth != null",
      ".write": false  // Admin only via Firebase Console
    },
    
    "price_history": {
      ".read": "auth != null",
      ".write": false
    },
    
    "users": {
      "$userId": {
        ".read": "$userId === auth.uid",
        ".write": "$userId === auth.uid",
        ".validate": "newData.hasChildren(['email', 'activeMarket'])"
      }
    },
    
    "vendor_chats": {
      "$marketId": {
        ".read": "auth != null",
        "messages": {
          ".write": "auth != null",
          "$messageId": {
            ".validate": "newData.hasChildren(['senderId', 'message', 'timestamp'])"
          }
        }
      }
    }
  }
}
4.3 Mock Data Seeding Script
Kotlin

// Firebase Admin SDK script to populate mock data
object FirebaseSeedScript {
    
    fun seedMarketPrices() {
        val markets = listOf("bangalore", "mumbai", "chennai", "pune", "delhi")
        val commodities = mapOf(
            "onion" to (18.0..28.0),
            "tomato" to (15.0..25.0),
            "potato" to (10.0..18.0),
            "green_chilli" to (30.0..45.0),
            "garlic" to (70.0..95.0)
        )
        
        val database = FirebaseDatabase.getInstance().reference
        val today = SimpleDateFormat("yyyy-MM-dd").format(Date())
        
        markets.forEach { market ->
            commodities.forEach { (commodity, priceRange) ->
                val randomPrice = priceRange.random()
                
                database.child("markets")
                    .child(market)
                    .child("prices")
                    .child(today)
                    .child(commodity)
                    .setValue(mapOf(
                        "price" to randomPrice,
                        "unit" to "kg",
                        "updated" to ServerValue.TIMESTAMP
                    ))
            }
        }
    }
    
    fun seedHistoricalPrices(market: String, commodity: String, days: Int = 7) {
        val database = FirebaseDatabase.getInstance().reference
        val calendar = Calendar.getInstance()
        var lastPrice = (15.0..25.0).random()
        
        repeat(days) { i ->
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val date = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
            
            // Simulate realistic price changes
            val priceChange = (-3.0..3.0).random()
            val newPrice = (lastPrice + priceChange).coerceIn(10.0, 40.0)
            val changePercent = ((newPrice - lastPrice) / lastPrice * 100)
            
            database.child("price_history")
                .child("${market}_${commodity}")
                .child("records")
                .child(date)
                .setValue(mapOf(
                    "price" to String.format("%.2f", newPrice).toDouble(),
                    "change" to String.format("%.1f", changePercent).toDouble()
                ))
            
            lastPrice = newPrice
        }
    }
}
4.4 Kotlin Repository Pattern
Kotlin

// PriceRepository.kt
class PriceRepository {
    private val database = FirebaseDatabase.getInstance().reference
    
    fun observePrices(market: String, date: String): Flow<List<CommodityPrice>> = callbackFlow {
        val pricesRef = database
            .child("markets")
            .child(market)
            .child("prices")
            .child(date)
        
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val prices = snapshot.children.mapNotNull { commoditySnapshot ->
                    val name = commoditySnapshot.key ?: return@mapNotNull null
                    val price = commoditySnapshot.child("price").getValue(Double::class.java) ?: return@mapNotNull null
                    val unit = commoditySnapshot.child("unit").getValue(String::class.java) ?: "kg"
                    
                    CommodityPrice(
                        name = name.replace("_", " ").capitalize(),
                        mandiPrice = price,
                        unit = unit,
                        market = market
                    )
                }
                trySend(prices)
            }
            
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        
        pricesRef.addValueEventListener(listener)
        
        awaitClose {
            pricesRef.removeEventListener(listener)
        }
    }
    
    fun getPriceHistory(market: String, commodity: String): Flow<List<PricePoint>> = callbackFlow {
        val historyRef = database
            .child("price_history")
            .child("${market}_${commodity}")
            .child("records")
            .orderByKey()
            .limitToLast(7)
        
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val history = snapshot.children.mapNotNull { dateSnapshot ->
                    val date = dateSnapshot.key ?: return@mapNotNull null
                    val price = dateSnapshot.child("price").getValue(Double::class.java) ?: return@mapNotNull null
                    val change = dateSnapshot.child("change").getValue(Double::class.java) ?: 0.0
                    
                    PricePoint(date, price, change)
                }.sortedBy { it.date }
                
                trySend(history)
            }
            
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        
        historyRef.addValueEventListener(listener)
        
        awaitClose {
            historyRef.removeEventListener(listener)
        }
    }
}

// Data Models
data class CommodityPrice(
    val name: String,
    val mandiPrice: Double,
    val unit: String,
    val market: String,
    val retailPrice: Double = 0.0,  // Calculated
    val trend: PriceTrend = PriceTrend.STABLE
)

data class PricePoint(
    val date: String,
    val price: Double,
    val change: Double
)

enum class PriceTrend {
    RISING, FALLING, STABLE
}
5. UI/UX DESIGN SYSTEM
5.1 Color Palette (Clean Brutalism)
Kotlin

// Color.kt
object SantePriceColors {
    // Primary Colors (Indigo Palette)
    val Indigo50 = Color(0xFFEEF2FF)
    val Indigo100 = Color(0xFFE0E7FF)
    val Indigo600 = Color(0xFF4F46E5)  // Primary brand color
    val Indigo700 = Color(0xFF4338CA)
    val Indigo900 = Color(0xFF312E81)
    
    // Neutral Colors (Slate Palette)
    val Slate50 = Color(0xFFF8FAFC)   // Background
    val Slate100 = Color(0xFFF1F5F9)
    val Slate200 = Color(0xFFE2E8F0)
    val Slate300 = Color(0xFFCBD5E1)
    val Slate400 = Color(0xFF94A3B8)
    val Slate500 = Color(0xFF64748B)
    val Slate600 = Color(0xFF475569)  // Body text
    val Slate700 = Color(0xFF334155)
    val Slate900 = Color(0xFF0F172A)  // Dark backgrounds
    
    // Digital Board Mode (High Contrast)
    val BoardBlack = Color(0xFF000000)     // Pure black
    val BoardYellow = Color(0xFFFFD600)    // Electric yellow
    
    // Semantic Colors
    val Success = Color(0xFF22C55E)   // Green-500 (price rising)
    val Danger = Color(0xFFEF4444)    // Red-500 (price falling)
    val Warning = Color(0xFFF59E0B)   // Amber-500
    val Info = Color(0xFF3B82F6)      // Blue-500
    
    // Functional Colors
    val White = Color(0xFFFFFFFF)
    val CardBackground = White
    val CardBorder = Slate200
}
5.2 Typography System
Kotlin

// Typography.kt
val OutfitFontFamily = FontFamily(
    Font(R.font.outfit_regular, FontWeight.Normal),
    Font(R.font.outfit_medium, FontWeight.Medium),
    Font(R.font.outfit_semibold, FontWeight.SemiBold),
    Font(R.font.outfit_bold, FontWeight.Bold),
    Font(R.font.outfit_extrabold, FontWeight.ExtraBold)
)

val InterFontFamily = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold)
)

val SantePriceTypography = Typography(
    // Display - For large numbers (prices, RRP)
    displayLarge = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 64.sp,
        lineHeight = 72.sp,
        letterSpacing = (-0.5).sp
    ),
    
    // Headlines - Screen titles
    headlineLarge = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    
    headlineMedium = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    
    // Titles - Card headers
    titleLarge = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    
    titleMedium = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    
    // Body - Primary text
    bodyLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    
    bodyMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    
    // Labels - Buttons, small UI elements
    labelLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    
    labelMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    
    labelSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
5.3 Component Specifications
Kotlin

// PriceCommodityCard.kt
@Composable
fun CommodityPriceCard(
    commodity: CommodityPrice,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = SantePriceColors.White
        ),
        border = BorderStroke(1.dp, SantePriceColors.CardBorder),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left section: Commodity info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Commodity name
                Text(
                    text = commodity.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = SantePriceColors.Slate900
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Price
                Row(
                    verticalAlignment = Alignment.Baseline
                ) {
                    Text(
                        text = "₹${String.format("%.2f", commodity.mandiPrice)}",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = SantePriceColors.Indigo600
                    )
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    Text(
                        text = "/${commodity.unit}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SantePriceColors.Slate500
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Source
                Text(
                    text = "${commodity.market.capitalize()} Mandi • Fresh Today",
                    style = MaterialTheme.typography.labelSmall,
                    color = SantePriceColors.Slate500
                )
            }
            
            // Right section: Trend indicator
            Icon(
                imageVector = when (commodity.trend) {
                    PriceTrend.RISING -> Icons.Default.TrendingUp
                    PriceTrend.FALLING -> Icons.Default.TrendingDown
                    PriceTrend.STABLE -> Icons.Default.Remove
                },
                contentDescription = "Price trend",
                tint = when (commodity.trend) {
                    PriceTrend.RISING -> SantePriceColors.Success
                    PriceTrend.FALLING -> SantePriceColors.Danger
                    PriceTrend.STABLE -> SantePriceColors.Slate400
                },
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// ProfitResultCard.kt
@Composable
fun ProfitResultCard(
    mandiPrice: Double,
    transportCost: Double,
    wastagePercent: Double,
    profitMargin: Double,
    modifier: Modifier = Modifier
) {
    // Calculations
    val landedCost = mandiPrice + transportCost
    val effectiveCost = landedCost / (1 - (wastagePercent / 100))
    val rrp = effectiveCost * (1 + (profitMargin / 100))
    val netProfit = rrp - effectiveCost
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = SantePriceColors.Slate900
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "RECOMMENDED PRICE",
                style = MaterialTheme.typography.labelLarge,
                color = SantePriceColors.BoardYellow,
                letterSpacing = 2.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // RRP Display
            Text(
                text = "₹${String.format("%.2f", rrp)}",
                style = MaterialTheme.typography.displayLarge,
                color = SantePriceColors.BoardYellow
            )
            
            Text(
                text = "per kg",
                style = MaterialTheme.typography.bodyLarge,
                color = SantePriceColors.Slate300
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            HorizontalDivider(
                color = SantePriceColors.Slate700,
                thickness = 1.dp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Breakdown
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Cost Breakdown",
                    style = MaterialTheme.typography.titleMedium,
                    color = SantePriceColors.Slate300
                )
                
                BreakdownRow("Mandi Price:", "₹${String.format("%.2f", mandiPrice)}")
                BreakdownRow("+ Transport:", "₹${String.format("%.2f", transportCost)}")
                
                HorizontalDivider(color = SantePriceColors.Slate700)
                
                BreakdownRow("Landed Cost:", "₹${String.format("%.2f", landedCost)}")
                BreakdownRow("+ Wastage (${wastagePercent.toInt()}%):", "₹${String.format("%.2f", effectiveCost - landedCost)}")
                
                HorizontalDivider(color = SantePriceColors.Slate700)
                
                BreakdownRow("Effective Cost:", "₹${String.format("%.2f", effectiveCost)}")
                BreakdownRow("+ Profit (${profitMargin.toInt()}%):", "₹${String.format("%.2f", rrp - effectiveCost)}")
                
                HorizontalDivider(
                    color = SantePriceColors.BoardYellow,
                    thickness = 2.dp
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "RETAIL PRICE:",
                        style = MaterialTheme.typography.titleMedium,
                        color = SantePriceColors.BoardYellow
                    )
                    Text(
                        text = "₹${String.format("%.2f", rrp)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = SantePriceColors.BoardYellow
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Profit highlight
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SantePriceColors.Success.copy(alpha = 0.2f)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SantePriceColors.Success,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Your Profit: ₹${String.format("%.2f", netProfit)} per kg",
                        style = MaterialTheme.typography.titleMedium,
                        color = SantePriceColors.Success
                    )
                }
            }
        }
    }
}

@Composable
fun BreakdownRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = SantePriceColors.Slate400
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = SantePriceColors.White
        )
    }
}
6. DEVELOPMENT CHECKLIST
Phase 1: Project Setup (Day 1-2)
 Create new Android Studio project with Kotlin
 Add Jetpack Compose dependencies
 Configure Firebase Realtime Database
 Set up Firebase Authentication (Google Sign-In)
 Add Outfit and Inter font families to res/font
 Create Color.kt and Typography.kt theme files
 Set up MVVM architecture folders:
ui/screens/
ui/components/
data/repository/
viewmodel/
 Configure Gradle with required libraries:
Firebase Realtime Database
Compose Navigation
MPAndroidChart
Retrofit (for potential API calls)
Phase 2: Authentication & Onboarding (Day 3-4)
 Implement Screen 1: Auth + Market Selection
 Firebase Google Sign-In integration
 Market selection grid UI
 Save selected market to Firebase user document
 Handle first-time vs returning user flow
 Test authentication end-to-end
Phase 3: Core Features - Price Watch (Day 5-7)
 Implement Screen 2: Price Watch Dashboard
 Firebase Realtime listener for prices
 Commodity price card component
 Trend indicator logic (rising/falling/stable)
 Smart Tip footer with dynamic messages
 Pull-to-refresh functionality
 Market switcher dropdown
 Seed mock data to Firebase
Phase 4: Core Features - Profit Calculator (Day 8-10)
 Implement Screen 3: Profit Calculator
 Input fields for Mandi price, transport cost
 Wastage percentage slider
 Profit margin slider
 Real-time RRP calculation engine
 Cost breakdown display card
 Financial literacy panel
 "Send to Price Board" functionality
 Unit tests for pricing algorithm
Phase 5: Trends & Analytics (Day 11-12)
 Implement Screen 4: Market Trends
 MPAndroidChart line chart integration
 7-day historical price visualization
 Seed historical price data to Firebase
 Gemini API integration (simulated)
 AI forecast text generation
 Commodity selector tabs
Phase 6: Digital Price Board (Day 13-15)
 Implement Screen 6: Digital Rate Board
 Full-screen immersive mode
 Yellow-on-black high-contrast design
 Font size controls (+/- buttons)
 WakeLock implementation (keep screen on)
 Auto max brightness on board open
 Mandi Mode toggle (dual price display)
 Test outdoor visibility (sunlight)
Phase 7: Navigation & Integration (Day 16-17)
 Bottom navigation bar implementation
 Screen-to-screen navigation flow
 Data passing between screens
 Deep linking (optional)
 Handle back press behavior
 Persistent state management
Phase 8: Testing & Refinement (Day 18-19)
 Unit tests for pricing formulas
 UI tests with Compose Testing
 Manual testing on physical device
 Test offline mode (cached prices)
 Firebase security rules testing
 Performance profiling (memory leaks)
 Accessibility audit (TalkBack)
Phase 9: Polish & Deployment (Day 20)
 App icon and splash screen
 Loading states and error handling
 Empty states design
 Generate signed APK
 Create demo video
 Write user documentation
 Final bug fixes
7. TESTING & DEPLOYMENT PROTOCOL
7.1 Test Scenarios
TEST CASE 1: Pricing Algorithm Accuracy
Objective: Verify cost-plus formula produces correct RRP

Steps:

Open Profit Calculator
Input: Mandi Price = ₹20, Transport = ₹3, Wastage = 15%, Profit = 25%
Check RRP output
Expected Result:

Landed Cost: ₹23.00
Effective Cost: ₹27.06
RRP: ₹33.82
Net Profit: ₹6.76/kg
Formula Validation:

text

Landed = 20 + 3 = 23
Effective = 23 / (1 - 0.15) = 27.06
RRP = 27.06 * (1 + 0.25) = 33.82
TEST CASE 2: Digital Board Readability
Objective: Ensure prices are readable from 2 meters in daylight

Steps:

Navigate to Digital Price Board
Set font size to default (72sp)
Place device 2 meters away
Test under:
Indoor lighting
Direct sunlight
Shade
Success Criteria:

All prices readable without squinting
Yellow-Black contrast ratio ≥ 7:1
No glare/reflection issues
TEST CASE 3: Real-time Price Updates
Objective: Verify Firebase listener updates UI instantly

Steps:

Open Price Watch screen
Manually update price in Firebase Console
Observe UI change
Expected Result:

Price updates within 2 seconds
No manual refresh needed
Smooth transition animation
TEST CASE 4: Offline Mode Graceful Degradation
Objective: App functions with cached data when offline

Steps:

Open app with internet connection
Load Price Watch screen
Enable Airplane Mode
Navigate to Profit Calculator
Perform calculation
Expected Result:

Last fetched prices still visible
Calculator works with cached data
"Offline" indicator shown
No app crashes
TEST CASE 5: Market Switching
Objective: Verify market selection updates all data sources

Steps:

Start with "Bangalore" market
Tap market dropdown in header
Select "Pune"
Check Price Watch prices
Expected Result:

All commodity prices update to Pune Mandi
Firebase query filters by new market
User preference saved to Firestore
7.2 Performance Benchmarks
METRIC	TARGET	CRITICAL
App Launch Time (Cold Start)	≤ 3 sec	≤ 5 sec
Price Feed Load Time	≤ 2 sec	≤ 4 sec
Calculator Real-time Recalculation	Instant	≤ 100ms
Screen Navigation Transition	≤ 300ms	≤ 500ms
Memory Usage (Idle)	≤ 50 MB	≤ 80 MB
APK Size	≤ 15 MB	≤ 25 MB
Digital Board Brightness Adjustment	Instant	≤ 200ms
7.3 Deployment Steps
Bash

# 1. Generate Signed APK
./gradlew assembleRelease

# 2. Verify APK
apksigner verify --verbose app-release.apk

# 3. Install on test device
adb install -r app-release.apk

# 4. Firebase App Distribution (Optional)
firebase appdistribution:distribute app/build/outputs/apk/release/app-release.apk \
  --app 1:YOUR_APP_ID:android:YOUR_HASH \
  --release-notes "v1.0 - Initial release with Profit Calculator and Digital Board" \
  --groups "beta-testers"
8. SAMPLE CODE SNIPPETS
8.1 Cost-Plus Pricing Engine
Kotlin

// PricingEngine.kt
object PricingEngine {
    
    data class PricingInput(
        val mandiPrice: Double,
        val transportCost: Double,
        val wastagePercent: Double,  // 0-100
        val profitMargin: Double     // 0-100
    )
    
    data class PricingOutput(
        val landedCost: Double,
        val effectiveCost: Double,
        val rrp: Double,
        val netProfit: Double,
        val grossRevenue: Double = 0.0  // If batch size provided
    )
    
    fun calculateRRP(input: PricingInput): PricingOutput {
        // Step 1: Landed Cost = Mandi Price + Transport
        val landedCost = input.mandiPrice + input.transportCost
        
        // Step 2: Effective Cost = Landed / (1 - Wastage%)
        val wastageMultiplier = 1 - (input.wastagePercent / 100)
        require(wastageMultiplier > 0) { "Wastage cannot be 100% or more" }
        val effectiveCost = landedCost / wastageMultiplier
        
        // Step 3: RRP = Effective * (1 + Profit%)
        val profitMultiplier = 1 + (input.profitMargin / 100)
        val rrp = effectiveCost * profitMultiplier
        
        // Step 4: Net Profit per unit
        val netProfit = rrp - effectiveCost
        
        return PricingOutput(
            landedCost = landedCost.roundTo2Decimals(),
            effectiveCost = effectiveCost.roundTo2Decimals(),
            rrp = rrp.roundTo2Decimals(),
            netProfit = netProfit.roundTo2Decimals()
        )
    }
    
    fun calculateBatchProfit(output: PricingOutput, batchSize: Double): PricingOutput {
        return output.copy(
            grossRevenue = (output.rrp * batchSize).roundTo2Decimals()
        )
    }
    
    private fun Double.roundTo2Decimals(): Double {
        return (this * 100).roundToInt() / 100.0
    }
}

// Unit Test
class PricingEngineTest {
    @Test
    fun `calculate RRP with standard inputs`() {
        val input = PricingEngine.PricingInput(
            mandiPrice = 20.0,
            transportCost = 3.0,
            wastagePercent = 15.0,
            profitMargin = 25.0
        )
        
        val output = PricingEngine.calculateRRP(input)
        
        assertEquals(23.0, output.landedCost, 0.01)
        assertEquals(27.06, output.effectiveCost, 0.01)
        assertEquals(33.82, output.rrp, 0.01)
        assertEquals(6.76, output.netProfit, 0.01)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `throw error if wastage is 100 percent`() {
        val input = PricingEngine.PricingInput(
            mandiPrice = 20.0,
            transportCost = 3.0,
            wastagePercent = 100.0,
            profitMargin = 25.0
        )
        
        PricingEngine.calculateRRP(input)
    }
}
8.2 Firebase Realtime Price Listener
Kotlin

// PriceWatchViewModel.kt
class PriceWatchViewModel(
    private val repository: PriceRepository
) : ViewModel() {
    
    private val _prices = MutableStateFlow<List<CommodityPrice>>(emptyList())
    val prices: StateFlow<List<CommodityPrice>> = _prices.asStateFlow()
    
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun loadPrices(market: String) {
        viewModelScope.launch {
            _loading.value = true
            
            repository.observePrices(market, getCurrentDate())
                .catch { exception ->
                    _error.value = exception.message
                    _loading.value = false
                }
                .collect { priceList ->
                    _prices.value = priceList.map { price ->
                        price.copy(trend = calculateTrend(price))
                    }
                    _loading.value = false
                }
        }
    }
    
    private suspend fun calculateTrend(price: CommodityPrice): PriceTrend {
        // Fetch yesterday's price from history
        val history = repository.getPriceHistory(price.market, price.name)
            .first()
        
        if (history.size < 2) return PriceTrend.STABLE
        
        val today = history.last().price
        val yesterday = history[history.size - 2].price
        val change = ((today - yesterday) / yesterday) * 100
        
        return when {
            change > 2.0 -> PriceTrend.RISING
            change < -2.0 -> PriceTrend.FALLING
            else -> PriceTrend.STABLE
        }
    }
    
    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date())
    }
}

// Screen Composable
@Composable
fun PriceWatchScreen(
    viewModel: PriceWatchViewModel = viewModel(),
    onCommodityClick: (String) -> Unit
) {
    val prices by viewModel.prices.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    val userMarket = remember { "bangalore" }  // Get from user preferences
    
    LaunchedEffect(userMarket) {
        viewModel.loadPrices(userMarket)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Price Watch") },
                actions = {
                    // Market dropdown
                    Text(
                        text = userMarket.capitalize(),
                        modifier = Modifier
                            .clickable { /* Show market selector */ }
                            .padding(16.dp)
                    )
                }
            )
        }
    ) { paddingValues ->
        when {
            loading && prices.isEmpty() -> {
                LoadingScreen()
            }
            
            error != null -> {
                ErrorScreen(message = error!!)
            }
            
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(prices, key = { it.name }) { commodity ->
                        CommodityPriceCard(
                            commodity = commodity,
                            onClick = { onCommodityClick(commodity.name) }
                        )
                    }
                    
                    // Smart Tip Footer
                    item {
                        SmartTipCard()
                    }
                }
            }
        }
    }
}
8.3 Gemini AI Integration (Simulated)
Kotlin

// GeminiService.kt
class GeminiService(private val apiKey: String) {
    
    private val client = OkHttpClient()
    private val gson = Gson()
    
    suspend fun generateForecast(
        commodity: String,
        priceHistory: List<Double>
    ): String = withContext(Dispatchers.IO) {
        
        val prompt = """
        You are a market analyst for Indian vegetable wholesale markets.
        
        Commodity: $commodity
        7-day price trend: ${priceHistory.joinToString(", ") { "₹%.2f".format(it) }}
        
        Task: Provide a one-sentence actionable forecast (max 20 words) 
        advising rural vendors whether to stock up now or wait.
        
        Be simple, direct, and helpful. Use phrases like "Buy now" or "Wait for drop".
        """.trimIndent()
        
        val requestBody = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", prompt))
                    })
                })
            })
            put("generationConfig", JSONObject().apply {
                put("temperature", 0.7)
                put("maxOutputTokens", 50)
            })
        }
        
        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey")
            .post(requestBody.toString().toRequestBody("application/json".toMediaType()))
            .build()
        
        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            
            if (!response.isSuccessful || responseBody == null) {
                return@withContext "Prices stable for next 24 hours."  // Fallback
            }
            
            val jsonResponse = JSONObject(responseBody)
            val forecast = jsonResponse
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")
            
            forecast.trim()
            
        } catch (e: Exception) {
            Log.e("Gemini", "Forecast generation failed", e)
            "Market data unavailable. Check back soon."
        }
    }
    
    // Simulated forecast for development (no API key needed)
    fun generateSimulatedForecast(priceHistory: List<Double>): String {
        if (priceHistory.size < 2) return "Insufficient data for forecast."
        
        val latest = priceHistory.last()
        val previous = priceHistory[priceHistory.size - 2]
        val trend = ((latest - previous) / previous) * 100
        
        return when {
            trend > 5 -> "Prices rising fast. Stock up now before further increase."
            trend > 0 -> "Slight uptrend. Good time to buy moderate stock."
            trend > -5 -> "Prices stable. Normal buying recommended."
            else -> "Prices falling. Wait 24h for better rates."
        }
    }
}
9. FINAL DELIVERABLES
What This SOP Provides:
✅ Complete Screen Prompts - AI-ready descriptions for all 6 screens
✅ Navigation Flow Diagram - Visual + code representation of screen connectivity
✅ Firebase Backend Blueprint - Realtime Database schema and security rules
✅ UI Component Library - Reusable Compose components with design tokens
✅ Development Roadmap - 20-day phase-by-phase checklist
✅ Testing Protocol - Test cases and success criteria
✅ Code Snippets - Critical implementation examples
How to Use This SOP with AI Agent:
Share entire README with AI agent (Claude, ChatGPT, Gemini)
Request: "Build Screen 1 - Auth using SCREEN_AUTH_MARKET_001 prompt"
For each subsequent screen, reference the prompt ID
For navigation: "Implement the navigation graph from Section 3"
For backend: "Set up Firebase Realtime Database per Section 4.1 schema"
For testing: "Write tests for TEST CASE 1-5 from Section 7.1"
10. PROJECT SUCCESS CRITERIA SUMMARY
✅ Profit Calculator accepts 3 independent inputs (transport, wastage, margin)
✅ RRP output matches manual formula verification (3+ test cases)
✅ Digital Board readable from 2 meters without zoom
✅ Yellow-on-Black contrast ratio ≥ 7:1 (WCAG AAA)
✅ First-time user reaches RRP view within 3 minutes
✅ Firebase price fetch completes in ≤3 seconds on 4G
✅ Offline mode displays cached prices for ≥24 hours
✅ UI functions on Android 8.0+ (API 26+) across 5-6.5" screens
✅ Cost-plus algorithm has zero calculation errors
✅ App promotes financial literacy (Gross vs Net profit education)
Document Version: 1.0
Last Updated: January 2025
Project Type: Android App Development using Generative AI
Domain: Micro-Finance & Rural Commerce



