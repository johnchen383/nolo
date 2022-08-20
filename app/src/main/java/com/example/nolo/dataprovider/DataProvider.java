package com.example.nolo.dataprovider;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.category.Category;
import com.example.nolo.entities.category.ICategory;
import com.example.nolo.entities.item.specs.Specs;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.purchasable.IPurchasable;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.entities.item.Accessory;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.storevariants.IStoreVariant;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.entities.item.Laptop;
import com.example.nolo.entities.item.Phone;
import com.example.nolo.entities.item.specs.AccessorySpecs;
import com.example.nolo.entities.item.specs.ISpecs;
import com.example.nolo.entities.item.specs.specsoption.ISpecsOption;
import com.example.nolo.entities.item.specs.LaptopSpecs;
import com.example.nolo.entities.item.specs.PhoneSpecs;
import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.entities.store.Branch;
import com.example.nolo.entities.store.IStore;
import com.example.nolo.entities.store.Store;
import com.example.nolo.entities.user.IUser;
import com.example.nolo.entities.user.User;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.enums.CollectionPath;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class DataProvider {
    private static void clearCollection(String collectionPath, Consumer<Void> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collectionPath).get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().delete();
                        }
                        Log.d("Firebase", "Cleared " + collectionPath);
                        callback.accept(null);
                    } else {
                        Log.i("Load Stores From Firebase", "Loading Stores collection failed from Firestore!");
                    }

                });
    }

    /**
     * Clears collection before adding entities
     * @param path
     * @param addEntityMethod
     */
    public static void clearAndAddEntity(String path, Consumer<Void> addEntityMethod){
        clearCollection(path, (a) -> addEntityMethod.accept(a));
    }

    /**
     * CATEGORIES
     */
    private static List<ICategory> generateCategories() {
        List<ICategory> categories = new ArrayList<>();

        categories.add(new Category(CategoryType.laptops, "category_laptop"));
        categories.add(new Category(CategoryType.phones, "category_phone"));
        categories.add(new Category(CategoryType.accessories, "category_accessory"));

        return categories;
    }

    public static void addCategoriesToFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<ICategory> categories = generateCategories();
        int count = 1;

        for (ICategory category : categories) {
            db.collection(CollectionPath.categories.name()).document("category " + count++).set(category).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.i("Add categories to Firebase", category.getCategoryName() + " added.");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("Add categories to Firebase", category + " NOT added.");
                }
            });
        }
    }

    /**
     * STORES
     */
    private static List<IStore> generateStores() {
        List<IStore> stores = new ArrayList<>();

        List<Branch> branches = new ArrayList<>();
        branches.add(new Branch("Albany", "14/100 Don Mckinnon Drive, Albany, Auckland", new GeoPoint(-36.73059303959944, 174.71132588248187)));
        branches.add(new Branch("Glenfield", "75 Porana Rd, Glenfield, Auckland", new GeoPoint(-36.78313087917839, 174.73992268248185)));
        branches.add(new Branch("Queen Street", "105 Queen St, Auckland CBD", new GeoPoint(-36.84602302115729, 174.76596971915174)));
        branches.add(new Branch("Auckland Uni", "Level 2, 2 Alfred Street, Auckland CBD, Auckland", new GeoPoint(-36.851872653551474, 174.76936556898895)));
        branches.add(new Branch("Penrose", "4 Station Rd, Penrose, Auckland", new GeoPoint(-36.908826077072405, 174.81423602481047)));
        branches.add(new Branch("St Lukes", "7A Wagener Place, Mount Albert, Auckland", new GeoPoint(-36.884680721220775, 174.73227526343936)));
        branches.add(new Branch("Manukau", "587 Great South Road, Manukau City, Auckland", new GeoPoint(-36.984429229816804, 174.8772683708388)));
        stores.add(new Store("PB Tech", branches));

        branches = new ArrayList<>();
        branches.add(new Branch("Albany", "Westfield Albany Shopping Centre, Level 1/219 Don McKinnon Drive, Albany, Auckland", new GeoPoint(-36.7215834701334, 174.70733641629718)));
        branches.add(new Branch("Wairau Park", "7 Link Drive, Wairau Valley, Auckland", new GeoPoint(-36.75790002613177, 174.74098204503818)));
        branches.add(new Branch("Lynn Mall", "Lynnmall Shopping Centre 3058 Great North Road, New Lynn, Auckland", new GeoPoint(-36.89091364943265, 174.68536376403185)));
        branches.add(new Branch("Sylvia Park", "Sylvia Park Shopping Centre T17, 286 Mount Wellington Highway, Mount Wellington, Auckland", new GeoPoint(-36.89860157564418, 174.84054564353116)));
        branches.add(new Branch("Botany", "Store G7/Ti Rakau Drive, East Tāmaki, Auckland", new GeoPoint(-36.922758587655785, 174.91195677392017)));
        branches.add(new Branch("Manukau", "Cnr Great South Rd & Wiri Station Road Westfield Manukau, Manukau City Centre, Auckland", new GeoPoint(-36.9765353409412, 174.8776244996947)));
        stores.add(new Store("JB Hi-Fi", branches));

        branches = new ArrayList<>();
        branches.add(new Branch("Newmarket", "309 Broadway, Newmarket, Auckland", new GeoPoint(-36.87160404552501, 174.7766327134929)));
        branches.add(new Branch("St Lukes Mega Centre", "Cnr St Lukes Rd & Wagener Plc, Mt Albert, Auckland", new GeoPoint(-36.883838462747725, 174.7305645730142)));
        branches.add(new Branch("Royal Oak", "100, Pah Road, Royal Oak, Auckland", new GeoPoint(-36.91205035527407, 174.77146029630026)));
        branches.add(new Branch("Lunn Ave", "95–111 Lunn Avenue, Mt Wellington, Auckland", new GeoPoint(-36.88747885940728, 174.83288401349293)));
        branches.add(new Branch("Sylvia Park", "Shop SM01C, Sylvia Park, Mt Wellington Highway, Auckland", new GeoPoint(-36.91856240165709, 174.84170448465724)));
        stores.add(new Store("Noel Leeming", branches));

        branches = new ArrayList<>();
        branches.add(new Branch("Mt Wellington", "20-54 Mount Wellington Highway Mt Wellington, Auckland", new GeoPoint(-36.898772785204734, 174.84560904855516)));
        branches.add(new Branch("Glenn Innes", "141 Apirana Avenue Glen Innes, Auckland", new GeoPoint(-36.87504295623851, 174.85346240661846)));
        branches.add(new Branch("Mt Roskill", "167-169 Stoddard Road, Auckland", new GeoPoint(-36.902112858084706, 174.72477059305703)));
        stores.add(new Store("Harvey Norman", branches));

        return stores;
    }

    public static void addStoresToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<IStore> stores = generateStores();

        for (IStore store : stores) {
            db.collection(CollectionPath.stores.name()).add(store).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        Log.i("Add stores to Firebase", task.getResult().getId() + " added.");
                    } else {
                        Log.i("Add stores to Firebase", store + " NOT added.");
                    }
                }
            });
        }
    }

    /**
     * USERS
     */
    private static List<IUser> generateUsers() {
        List<IUser> users = new ArrayList<>();
        List<IItemVariant> history = new ArrayList<>();
        List<IPurchasable> cart = new ArrayList<>();
        IUser u;

        history.add(new ItemVariant());
        cart.add(new Purchasable());
        u = new User(history, cart);
        u.setEmail("john.bm.chen@gmail.com");
        users.add(u);

        u = new User(history, cart);
        u.setEmail("nick@gmail.com");
        users.add(u);

        return users;
    }

    public static void addUsersToFirestore() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<IUser> users = generateUsers();

        for (IUser user : users) {
            auth.createUserWithEmailAndPassword(user.getEmail(), "password123").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        user.setUserAuthUid(task.getResult().getUser().getUid());
                        Log.i("Sign Up", "createUserWithEmail:success");

                        db.collection(CollectionPath.users.name()).document(user.getUserAuthUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i("Add users to Firebase", user.getUserAuthUid() + " added.");
                                } else {
                                    Log.i("Add users to Firebase", user + " NOT added.");
                                }
                            }
                        });
                    } else {
                        Log.i("Sign Up", "createUserWithEmail:failure", task.getException());
                    }
                }
            });
        }
    }

    /**
     * ITEMS
     */
    private static List<IItem> generateItems() {
        List<IItem> items = new ArrayList<>();
        List<String> imageUris, recommendedAccessoryIds;
        Specs specs;
        List<SpecsOption> rams, storages;
        List<StoreVariant> itemStoreVariant;
        List<Colour> colours;

        /**
         * Laptops
         */
        /*
         * Laptop 1
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("silver", "#BEBEBE"),
                new Colour("starlight", "#D6CEC3"),
                new Colour("grey", "#646569"),
                new Colour("midnight", "#333A44")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 2149),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 2499),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 2249),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 2199)
        ));
        rams = new ArrayList<>(Arrays.asList(
                new SpecsOption(8, 0),
                new SpecsOption(16, 350),
                new SpecsOption(24, 700)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(256, 0),
                new SpecsOption(512, 350),
                new SpecsOption(1024, 700),
                new SpecsOption(2048, 1400)
        ));
        specs = new LaptopSpecs("MacBook Air M2 chip model",
                "macOS",
                "13.6-inch (diagonal) LED-backlit display with IPS technology; (2560*1664), Liquid Retina display",
                "Apple M2 chip; 8-core CPU with four performance cores and four efficiency cores",
                "Integrated Graphics 8-core GPU",
                rams,
                storages,
                "1080p FaceTime HD camera",
                "Backlit Magic Keyboard",
                "802.11ax Wi-Fi 6 wireless networking + Bluetooth 5.0 wireless technology",
                "Four-speaker sound system; Wide stereo sound",
                "N/A",
                "Touch ID",
                "N/A",
                "Thunderbolt 3 digital video output; Native DisplayPort output over USB‑C",
                "52.6-watt‑hour lithium‑polymer battery",
                "30W USB-C Power Adapter",
                "304.1 x 215 x 11.3 mm",
                "1.24 kg");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_macbook_air_m2_1_silver",
                "item_laptop_macbook_air_m2_2_silver",
                "item_laptop_macbook_air_m2_3_silver",

                "item_laptop_macbook_air_m2_1_starlight",
                "item_laptop_macbook_air_m2_2_starlight",
                "item_laptop_macbook_air_m2_3_starlight",

                "item_laptop_macbook_air_m2_1_grey",
                "item_laptop_macbook_air_m2_2_grey",
                "item_laptop_macbook_air_m2_3_grey",

                "item_laptop_macbook_air_m2_1_midnight",
                "item_laptop_macbook_air_m2_2_midnight",
                "item_laptop_macbook_air_m2_3_midnight"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Laptop("MacBook Air (M2)",
                "Apple",
                (LaptopSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Laptop 2
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("silver", "#BEBEBE"),
                new Colour("grey", "#646569")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 4299),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 4199),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 4359),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 4299)
        ));
        rams = new ArrayList<>(Arrays.asList(
                new SpecsOption(16, 0),
                new SpecsOption(24, 700)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(512, 0),
                new SpecsOption(1024, 350),
                new SpecsOption(2048, 1050),
                new SpecsOption(4096, 2100),
                new SpecsOption(8192, 4200)
        ));
        specs = new LaptopSpecs("MacBook Pro 16\" models",
                "macOS",
                "16.2-inch (diagonal) Liquid Retina XDR display, (3456*2234)",
                "Apple M1 Pro chip; 10-core CPU with eight performance cores and two efficiency cores",
                "Integrated Graphics 16-core GPU",
                rams,
                storages,
                "1080p FaceTime HD camera",
                "Backlit Magic Keyboard",
                "802.11ax Wi-Fi 6 wireless networking + Bluetooth 5.0 wireless technology",
                "High-fidelity six-speaker sound system with force-cancelling woofers; Wide stereo sound",
                "N/A",
                "Touch ID",
                "N/A",
                "SDXC card slot; HDMI port; 3.5-mm headphone jack; MagSafe 3 port; Three Thunderbolt 4 (USB-C) ports",
                "100-watt-hour lithium-polymer battery",
                "140W USB-C Power Adapter",
                "355.7 x 248.1 x 16.8 mm",
                "2.15 kg");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_macbook_pro_16_1_silver",
                "item_laptop_macbook_pro_16_2_silver",
                "item_laptop_macbook_pro_16_3_silver",

                "item_laptop_macbook_pro_16_1_grey",
                "item_laptop_macbook_pro_16_2_grey",
                "item_laptop_macbook_pro_16_3_grey"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Laptop("MacBook Pro 16\"",
                "Apple",
                (LaptopSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Laptop 3
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000"),
                new Colour("blue", "#9A9CA3"),
                new Colour("sandstone", "#79838A"),
                new Colour("platinum", "#CBB1A0")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 1749),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 1699),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 1798)
        ));
        rams = new ArrayList<>(Arrays.asList(
                new SpecsOption(8, 0),
                new SpecsOption(16, 400)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(256, 0),
                new SpecsOption(512, 450)
        ));
        specs = new LaptopSpecs("Microsoft Surface Laptop 4 13.5\"",
                "Windows 11 Home",
                "13.5\" 2256 x 1504 (201 PPI)",
                "AMD Ryzen™ 5 4680U Mobile Processor with Radeon™ Graphics Microsoft Surface® Edition (6 cores)",
                "AMD Ryzen™ Microsoft Surface® Edition: AMD Radeon™ Graphics",
                rams,
                storages,
                "Windows Hello face authentication camera (front-facing); 720p HD f2.0 camera (front-facing)",
                "Backlight",
                "Wi-Fi 6: 802.11ax + Bluetooth® Wireless 5.0 technology",
                "Dual far-field Studio Mics; Omnisonic Speakers with Dolby Atmos",
                "10 point multi-touch",
                "N/A",
                "N/A",
                "1 x USB-C; 1 x USB-A; 3.5 mm headphone jack; 1 x Surface Connect port",
                "Battery Capacity Nominal (WH) 47.4",
                "Power Supply",
                "308 x 223 x 14.5 mm",
                "1.27 kg");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_surface_laptop_4_1_black",
                "item_laptop_surface_laptop_4_2_black",
                "item_laptop_surface_laptop_4_3_black",

                "item_laptop_surface_laptop_4_1_blue",
                "item_laptop_surface_laptop_4_2_blue",
                "item_laptop_surface_laptop_4_3_blue",

                "item_laptop_surface_laptop_4_1_sandstone",
                "item_laptop_surface_laptop_4_2_sandstone",
                "item_laptop_surface_laptop_4_3_sandstone",

                "item_laptop_surface_laptop_4_1_platinum",
                "item_laptop_surface_laptop_4_2_platinum",
                "item_laptop_surface_laptop_4_3_platinum"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Laptop("Surface Laptop 4",
                "Microsoft",
                (LaptopSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Laptop 4
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("platinum", "#86888A"),
                new Colour("graphite", "#525556")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 1849),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 1899),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 1879),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 1849)
        ));
        rams = new ArrayList<>(Arrays.asList(
                new SpecsOption(8, 0),
                new SpecsOption(16, 180),
                new SpecsOption(32, 129)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(128, 0),
                new SpecsOption(256, 180),
                new SpecsOption(512, 320),
                new SpecsOption(1024, 520)
        ));
        specs = new LaptopSpecs("Microsoft Surface Pro 8",
                "Windows 11 Home",
                "13\" 2880 x 1920 (267 PPI) 120Hz",
                "Quad-core 11th Gen Intel® Core™ i5-1135G7 Processor, designed on the Intel® Evo™ platform",
                "Integrated Graphics",
                rams,
                storages,
                "Windows Hello face authentication camera (front-facing); 5.0MP front-facing camera with 1080p full HD video; 10.0MP rear-facing autofocus camera with 1080p HD and 4k video",
                "Backlight",
                "Wi-Fi 6: 802.11ax + Bluetooth® Wireless 5.1 technology",
                "Dual far-field Studio Mics; 2W stereo speakers with Dolby Atmos",
                "10 point multi-touch",
                "N/A",
                "N/A",
                "2 x USB-C® with USB 4.0/Thunderbolt™ 4; 3.5mm headphone jack; 1 × Surface Connect port; Surface Type Cover port",
                "Battery Capacity Nominal (WH) 51.5Wh",
                "Power Supply",
                "287 x 208 x 9.3 mm",
                "891 g");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_surface_pro_8_1_platinum",
                "item_laptop_surface_pro_8_2_platinum",
                "item_laptop_surface_pro_8_3_platinum",

                "item_laptop_surface_pro_8_1_graphite",
                "item_laptop_surface_pro_8_2_graphite",
                "item_laptop_surface_pro_8_3_graphite"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Laptop("Surface Pro 8",
                "Microsoft",
                (LaptopSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Laptop 5
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("blue", "#565D72")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 740.99),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 727.99)
        ));
        rams = new ArrayList<>(Arrays.asList(
                new SpecsOption(4, 0),
                new SpecsOption(8, 59)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(256, 0),
                new SpecsOption(512, 99)
        ));
        specs = new LaptopSpecs("ASUS Vivobook Go 14 Flip TP1400KA Laptop 14\" HD Touch",
                "Windows 11 Home",
                "14.0-inch, FHD (1920 x 1080) 16:9 aspect ratio",
                "Intel® Celeron® N4500 Processor 1.1 GHz (4M Cache, up to 2.8 GHz, 2 cores)\n",
                "Intel® UHD Graphics",
                rams,
                storages,
                "720p HD camera",
                "Backlit Chiclet Keyboard; 1.4mm Key-travel",
                "Wi-Fi 5(802.11ac) (Dual band) 1*1 + Bluetooth 4.1",
                "SonicMaster; Built-in speaker; Built-in array microphone with Cortana support",
                "Touch screen display",
                "Fingerprint sensor integrated with Touchpad",
                "N/A",
                "1x USB 2.0 Type-A; 1x USB 3.2 Gen 1 Type-A; 1x USB 3.2 Gen 1 Type-C; 1x HDMI 1.4; 1x 3.5mm Combo Audio Jack; 1x DC-in; Micro SD card reader",
                "39WHrs, 2S1P, 2-cell Li-ion",
                "45W AC Adapter",
                "32.07 x 21.70 x 1.69 mm",
                "1.50 kg");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_asus_vivobook_go_14_flip_tp1400ka_1_blue",
                "item_laptop_asus_vivobook_go_14_flip_tp1400ka_2_blue",
                "item_laptop_asus_vivobook_go_14_flip_tp1400ka_3_blue"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Laptop("ASUS Vivobook Go 14 Flip TP1400KA",
                "ASUS",
                (LaptopSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Laptop 6
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 2562.39),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 2562.39),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 2562.39),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 2562.39)
        ));
        rams = new ArrayList<>(Arrays.asList(
                new SpecsOption(16, 0)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(256, 0),
                new SpecsOption(512, 126.5),
                new SpecsOption(1024, 586.5),
                new SpecsOption(2048, 1161.5)
        ));
        specs = new LaptopSpecs("Lenovo ThinkPad X1 Carbon Gen 10 - Intel® Evo",
                "Windows 11 Home",
                "14\" WUXGA (1920 x 1200); IPS; Anti-Glare; Non-Touch; 100%sRGB; 400 nits; Narrow Bezel; Low Blue Light",
                "12th Generation Intel® Core™ i5-1240P Processor (E-cores up to 3.30 GHz P-cores up to 4.40 GHz)",
                "Integrated Intel® Iris® Xe Graphics",
                rams,
                storages,
                "FHD IR/RGB Hybrid with Microphone",
                "Backlit; Black with Fingerprint Reader - English",
                "Wi-Fi 6E AX211 2x2 AX & Bluetooth® 5.0",
                "Audio jack; Two upward- and two downward-firing speakers",
                "N/A",
                "Fingerprint Reader",
                "N/A",
                "2x USB-C Thunderbolt 4 ports; 2x USB 3.2 Type-A port; 1x HDMI video output; 1x nano SIM slot",
                "4 Cell Li-Polymer Internal Battery, 57Wh",
                "65W adapter",
                "323.5 x 217.1 x 15.95 mm",
                "1.13 kg");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_lenovo_thinkpad_x1_1_black",
                "item_laptop_lenovo_thinkpad_x1_2_black",
                "item_laptop_lenovo_thinkpad_x1_3_black"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Laptop("Lenovo ThinkPad X1 Carbon Gen 10 - Intel® Evo",
                "Lenovo",
                (LaptopSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Laptop 7
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("grey", "#787878"),
                new Colour("white", "#000000")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 3311),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 3300),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 3211)
        ));
        rams = new ArrayList<>(Arrays.asList(
                new SpecsOption(8, 0),
                new SpecsOption(16, 69),
                new SpecsOption(32, 129)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(256, 0),
                new SpecsOption(512, 99),
                new SpecsOption(1024, 179)
        ));
        specs = new LaptopSpecs("Legion 5 Pro (16\", Gen 7) AMD",
                "Windows 11 Home",
                "16.0\" WQXGA (2560x1600) IPS 500nits Anti-glare; 165Hz",
                "AMD Ryzen™ 7 6800H",
                "NVIDIA® GeForce RTX™ 3050 Ti 4GB GDDR6",
                rams,
                storages,
                "HD 720p, with E-camera shutter, fixed focus",
                "6-row, multimedia Fn keys, numeric keypad, black keycap",
                "802.11AX (2x2) & Bluetooth® 5.1",
                "Stereo speakers; 2 x 2W, Nahimic Audio; Dual array mic",
                "Non-touch",
                "No fingerprint reader",
                "N/A",
                "2 x USB 3.2 Gen 1; 1 x USB 3.2 Gen 1 (Always On); 2 x USB-C 3.2 Gen 2 (support data transfer and DisplayPort™ 1.4); 1 x USB-C 3.2 Gen 2 (support data transfer; Power Delivery 135W and DisplayPort 1.4); 1 x HDMI 2.1; 1 x Ethernet (RJ-45); 1 x Headphone / mic; 1 x Power connector",
                "4-cell (80Wh), integrated",
                "230W Slim Tip (3-pin)",
                "359.9 x 262.4 x 19.9 mm",
                "2.49 kg");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_legion_5_pro_16arh7_1_grey",
                "item_laptop_legion_5_pro_16arh7_2_grey",
                "item_laptop_legion_5_pro_16arh7_3_grey",

                "item_laptop_legion_5_pro_16arh7_1_white",
                "item_laptop_legion_5_pro_16arh7_2_white",
                "item_laptop_legion_5_pro_16arh7_3_white"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Laptop("Legion 5 Pro 16ARH7",
                "Lenovo",
                (LaptopSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Laptop 8
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 1299),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 1322),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 1300)
        ));
        rams = new ArrayList<>(Arrays.asList(
                new SpecsOption(8, 0),
                new SpecsOption(16, 59),
                new SpecsOption(32, 129)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(256, 0),
                new SpecsOption(512, 79),
                new SpecsOption(1024, 129)
        ));
        specs = new LaptopSpecs("MSI GF63 Thin 11SC GTX1650 Max Q Gaming Laptop 15.6' FHD",
                "Windows 11 Home",
                "15.6\" FHD (1920*1080) 60Hz",
                "11th Gen. Intel® Core™ i5-11400H Processor 6 Cores",
                "NVIDIA® GeForce® GTX 1650 Laptop GPU, 4GB GDDR6",
                rams,
                storages,
                "HD type (30fps @ 720p)",
                "Backlight Keyboard (Single-Color, Red)",
                "802.11 ax Wi-Fi 6 + Bluetooth v5.2",
                "2x 2W Speaker; 1x Mic-in; 1x Headphone-out",
                "N/A",
                "N/A",
                "N/A",
                "1x RJ45; 1x (4K @ 30Hz) HDMI; 1x Type-C USB3.2 Gen1; 3x Type-A USB3.2 Gen1",
                "3-Cell, 51 Battery (Whr)",
                "120W adapter",
                "359 x 254 x 21.7 mm",
                "1.86 kg");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_msi_gf63_1_black",
                "item_laptop_msi_gf63_2_black",
                "item_laptop_msi_gf63_3_black"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Laptop("MSI GF63",
                "MSI",
                (LaptopSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Laptop 9
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 3199.99),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 3199.99)
        ));
        rams = new ArrayList<>(Arrays.asList(
                new SpecsOption(16, 0),
                new SpecsOption(32, 300)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(1024, 0)
        ));
        specs = new LaptopSpecs("Razer Blade 17 - Full HD 360Hz - GeForce RTX 3070 Ti - Black",
                "Windows 11 Home",
                "17.3\" FHD 360Hz; 100% sRGB; 6mm bezels; individually factory calibrated; 3ms Response Rate",
                "1.8GHz 14-core Intel i7-12800H processor; Turbo Boost up to 4.8GHz, with 24MB of Cache",
                "NVIDIA® GeForce RTX™ 3070 Ti (8GB GDDR6 VRAM)",
                rams,
                storages,
                "Built-in Full HD webcam",
                "Per-Key Backlighting, powered by Razer Chroma",
                "Wireless Wi-Fi 6E AX1690 (IEEE 802.11a/b/g/n/ac/ax/az); Bluetooth® 5.2\n",
                "3.5mm Combo-Jack; Stereo 2.0 | 8 Speakers; THX Spatial Audio; 2-Mic Array",
                "N/A",
                "N/A",
                "N/A",
                "2 x Thunderbolt™ 4 (USB-C™); 1 x USB-C 3.2 Gen 2 - Supports Power Delivery 3 (15W); 3 x USB-A 3.2 Gen 2",
                "Built-in 82WHr rechargeable lithium-ion polymer battery",
                "280W power adapter",
                "395 x 260 x 19.9 mm",
                "2.75 kg");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_razer_blade_17_1_black",
                "item_laptop_razer_blade_17_2_black",
                "item_laptop_razer_blade_17_3_black"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Laptop("Razer Blade 17 - Full HD 360Hz - GeForce RTX 3070 Ti - Black",
                "Razer",
                (LaptopSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Laptop 10
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 1429),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 1409),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 1369),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 1399)
        ));
        rams = new ArrayList<>(Arrays.asList(
                new SpecsOption(8, 0),
                new SpecsOption(16, 69)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(512, 0)
        ));
        specs = new LaptopSpecs("Acer Aspire 7 A715-42G-R0EX",
                "Windows 10 Home",
                "39.6 cm (15.6\") LCD",
                "AMD Ryzen 5 5500U Hexa-core (6 Core™) 2.10 GHz; up to 4 GHz; 8 MB Cache",
                "NVIDIA® GeForce® GTX 1650 4 GB GDDR6",
                rams,
                storages,
                "Front Camera/Webcam, 1280 x 720",
                "Backlight",
                "802.11 ax Wi-Fi 6 + Bluetooth",
                "Microphone; Stereo speakers",
                "N/A",
                "Yes",
                "N/A",
                "1x HDMI; 1x USB 2.0 Ports; 2x USB 3.2 Gen 1 Type-A Ports; 1x USB 3.2 Gen 1 Type-C Ports (up to 5 Gbps); 1x Network (RJ-45); 1x Headphone/Microphone Combo Port",
                "3-cell Lithium Ion (Li-Ion) 48 Wh",
                "135W adapter",
                "363.4 x 254.5 x 22.9 mm",
                "2.15 kg");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_acer_aspire_7_a715_42g_roex_1_black",
                "item_laptop_acer_aspire_7_a715_42g_roex_2_black",
                "item_laptop_acer_aspire_7_a715_42g_roex_3_black"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Laptop("Acer Aspire 7 A715-42G-R0EX",
                "Acer",
                (LaptopSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));


        /**
         * Phones
         */
        /*
         * Phone 1
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("burgundy", "#6A4E57"),
                new Colour("black", "#000000"),
                new Colour("white", "#FFFFFF"),
                new Colour("green", "#507974")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 1999),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 1899),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 2099),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 1999)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(128, 0),
                new SpecsOption(256, 100),
                new SpecsOption(512, 400)
        ));
        specs = new PhoneSpecs("Samsung Galaxy S22 Ultra",
                "Android 12",
                "6.8\" edge Quad HD+ Dynamic AMOLED 2X; Infinity-O Display (3088x1440); 120Hz refresh rate",
                "Snapdragon 8 Gen 1 4nm octa-core flagship processor",
                storages,
                "Quad Rear Camera; 108MP Main sensor, F1.8, OIS; 12MP Ultra Wide angle, F2.2; 10MP 3x Telephoto, F2.4, OIS; 10MP 10x Periscope Telephoto, F4.9, OIS; Up to 100x Space Zoom; 40MP Front camera, F2.2",
                "5G; LTE; Wi-Fi 802.11; Bluetooth v5.2",
                "Stereo speakers; Ultra high quality audio playback; Audio playback format",
                "Yes",
                "IP68",
                "Dual SIM model",
                "Ultrasonic Fingerprint sensor; Geomagnetic sensor; Accelerometer; Hall sensor; Barometer; Proximity sensor; Gyro sensor; Ambient Light sensor",
                "5000mAh",
                "77.9 x 163.3 x 8.9mm",
                "228g");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_phone_samsung_galaxy_s22_ultra_1_burgundy",
                "item_phone_samsung_galaxy_s22_ultra_2_burgundy",
                "item_phone_samsung_galaxy_s22_ultra_3_burgundy",

                "item_phone_samsung_galaxy_s22_ultra_1_black",
                "item_phone_samsung_galaxy_s22_ultra_2_black",
                "item_phone_samsung_galaxy_s22_ultra_3_black",

                "item_phone_samsung_galaxy_s22_ultra_1_white",
                "item_phone_samsung_galaxy_s22_ultra_2_white",
                "item_phone_samsung_galaxy_s22_ultra_3_white",

                "item_phone_samsung_galaxy_s22_ultra_1_green",
                "item_phone_samsung_galaxy_s22_ultra_2_green",
                "item_phone_samsung_galaxy_s22_ultra_3_green"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Phone("Samsung Galaxy S22 Ultra",
                "Samsung",
                (PhoneSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Phone 2
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("purple", "#B2A1CD"),
                new Colour("white", "#E9E9E7"),
                new Colour("green", "#587876"),
                new Colour("pink", "#E3D2CF"),
                new Colour("black", "#000000")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 1299),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 1298.99),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 1299),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 1299)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(128, 0),
                new SpecsOption(256, 100)
        ));
        specs = new PhoneSpecs("Samsung Galaxy S22",
                "Android 12",
                "6.1\" Dynamic AMOLED 2X display; FHD+ (2340x1080); 120Hz refresh rate",
                "Snapdragon 8 Gen 1 4nm octa-core flagship processor",
                storages,
                "Triple Rear Camera; 50MP main sensor, F1.8, OIS; 12MP Ultra-Wide angle, F2.2; 10MP 3x Telephoto, F2.4, OIS; 30x Digital Zoom; 10MP Front camera, F2.2, Autofocus",
                "5G; LTE; Wi-Fi 802.11; Bluetooth v5.2",
                "Duel stereo speakers",
                "Yes",
                "IP68",
                "Dual SIM model",
                "Ultrasonic Fingerprint sensor; Geomagnetic sensor; Accelerometer; Hall sensor; Barometer; Proximity sensor; Gyro sensor; Ambient Light sensor",
                "3700mAh",
                "70.6 x 146 x 7.6mm",
                "167g");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_phone_samsung_galaxy_s22_1_purple",
                "item_phone_samsung_galaxy_s22_2_purple",
                "item_phone_samsung_galaxy_s22_3_purple",

                "item_phone_samsung_galaxy_s22_1_white",
                "item_phone_samsung_galaxy_s22_2_white",
                "item_phone_samsung_galaxy_s22_3_white",

                "item_phone_samsung_galaxy_s22_1_green",
                "item_phone_samsung_galaxy_s22_2_green",
                "item_phone_samsung_galaxy_s22_3_green",

                "item_phone_samsung_galaxy_s22_1_pink",
                "item_phone_samsung_galaxy_s22_2_pink",
                "item_phone_samsung_galaxy_s22_3_pink",

                "item_phone_samsung_galaxy_s22_1_black",
                "item_phone_samsung_galaxy_s22_2_black",
                "item_phone_samsung_galaxy_s22_3_black"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Phone("Samsung Galaxy S22",
                "Samsung",
                (PhoneSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Phone 3
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("green", "#576856"),
                new Colour("silver", "#F2F3EE"),
                new Colour("gold", "#FAEAD3"),
                new Colour("graphite", "#5F5E5A"),
                new Colour("blue", "#AFC6DC")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 1999),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 1958.99),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 1999),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 1958.99)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(128, 0),
                new SpecsOption(256, 200),
                new SpecsOption(512, 600),
                new SpecsOption(1024, 1000)
        ));
        specs = new PhoneSpecs("Apple iPhone 13 Pro Max",
                "iOS 15",
                "6.7‑inch (diagonal) all‑screen OLED display; Super Retina XDR display; (2778x1284) pixel resolution at 458 ppi",
                "A15 Bionic chip",
                storages,
                "Telephoto, Wide and Ultra Wide cameras; 3x optical zoom in, 2x optical zoom out; 6x optical zoom range; Digital zoom up to 15x; Night mode portraits enabled by LiDAR Scanner",
                "5G; LTE; Wi-Fi 802.11ax; Bluetooth 5.0",
                "Stereo speakers; Spatial audio playback",
                "Yes",
                "IP68",
                "Dual SIM (nano‑SIM and eSIM); Dual eSIM support",
                "Face ID; LiDAR Scanner; Barometer; Three‑axis gyro; Accelerometer; Proximity sensor; Ambient light sensor",
                "Built‑in rechargeable lithium‑ion battery",
                "78.1 x 160.8 x 7.65mm",
                "238g");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_phone_iphone_13_pro_max_1_green",
                "item_phone_iphone_13_pro_max_2_green",
                "item_phone_iphone_13_pro_max_3_green",

                "item_phone_iphone_13_pro_max_1_silver",
                "item_phone_iphone_13_pro_max_2_silver",
                "item_phone_iphone_13_pro_max_3_silver",

                "item_phone_iphone_13_pro_max_1_gold",
                "item_phone_iphone_13_pro_max_2_gold",
                "item_phone_iphone_13_pro_max_3_gold",

                "item_phone_iphone_13_pro_max_1_graphite",
                "item_phone_iphone_13_pro_max_2_graphite",
                "item_phone_iphone_13_pro_max_3_graphite",

                "item_phone_iphone_13_pro_max_1_blue",
                "item_phone_iphone_13_pro_max_2_blue",
                "item_phone_iphone_13_pro_max_3_blue"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Phone("iPhone 13 Pro Max",
                "Apple",
                (PhoneSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Phone 4
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("green", "#495A48"),
                new Colour("pink", "#FBE2DD"),
                new Colour("blue", "#447792"),
                new Colour("midnight", "#3F464C"),
                new Colour("starlight", "#FBF7F4"),
                new Colour("red", "#C92435")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 1429),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 1428.99),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 1429),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 1428.99)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(128, 0),
                new SpecsOption(256, 200),
                new SpecsOption(512, 600)
        ));
        specs = new PhoneSpecs("Apple iPhone 13",
                "iOS 15",
                "6.1‑inch (diagonal) all‑screen OLED display; Super Retina XDR display; (2532x1170) pixel resolution at 460 ppi",
                "A15 Bionic chip",
                storages,
                "Wide and Ultra Wide cameras; 2x optical zoom out; Digital zoom up to 5x",
                "5G; LTE; Wi-Fi 802.11ax; Bluetooth 5.0",
                "Stereo speakers; Spatial audio playback",
                "Yes",
                "IP68",
                "Dual SIM (nano‑SIM and eSIM); Dual eSIM support",
                "Face ID; Barometer; Three‑axis gyro; Accelerometer; Proximity sensor; Ambient light sensor",
                "Built‑in rechargeable lithium‑ion battery",
                "71.5 x 146.7 x 7.65mm",
                "173g");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_phone_iphone_13_1_green",
                "item_phone_iphone_13_2_green",
                "item_phone_iphone_13_3_green",

                "item_phone_iphone_13_1_pink",
                "item_phone_iphone_13_2_pink",
                "item_phone_iphone_13_3_pink",

                "item_phone_iphone_13_1_blue",
                "item_phone_iphone_13_2_blue",
                "item_phone_iphone_13_3_blue",

                "item_phone_iphone_13_1_midnight",
                "item_phone_iphone_13_2_midnight",
                "item_phone_iphone_13_3_midnight",

                "item_phone_iphone_13_1_starlight",
                "item_phone_iphone_13_2_starlight",
                "item_phone_iphone_13_3_starlight",

                "item_phone_iphone_13_1_red",
                "item_phone_iphone_13_2_red",
                "item_phone_iphone_13_3_red"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Phone("iPhone 13",
                "Apple",
                (PhoneSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Phone 5
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("midnight", "#3F464C"),
                new Colour("starlight", "#FBF7F4"),
                new Colour("red", "#C92435")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 799),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 799),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 799),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 799)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(64, 0),
                new SpecsOption(128, 100),
                new SpecsOption(256, 300)
        ));
        specs = new PhoneSpecs("Apple iPhone SE",
                "iOS 15",
                "4.7-inch (diagonal) widescreen LCD; Retina HD display; (1334x750) pixel resolution at 326 ppi",
                "A15 Bionic chip",
                storages,
                "12MP Wide camera; Digital zoom up to 5x",
                "5G; LTE; Wi-Fi 802.11ax; Bluetooth 5.0",
                "Stereo speakers; Spatial audio playback",
                "Yes",
                "IP67",
                "Dual SIM (nano‑SIM and eSIM); Dual eSIM support",
                "Touch ID fingerprint sensor; Barometer; Three‑axis gyro; Accelerometer; Proximity sensor; Ambient light sensor",
                "Built‑in rechargeable lithium‑ion battery",
                "67.3 x 138.4 x 7.3mm",
                "144g");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_phone_iphone_se_1_midnight",
                "item_phone_iphone_se_2_midnight",
                "item_phone_iphone_se_3_midnight",

                "item_phone_iphone_se_1_starlight",
                "item_phone_iphone_se_2_starlight",
                "item_phone_iphone_se_3_starlight",

                "item_phone_iphone_se_1_red",
                "item_phone_iphone_se_2_red",
                "item_phone_iphone_se_3_red"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Phone("iPhone SE",
                "Apple",
                (PhoneSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Phone 6
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("emerald", "#4F8D78"),
                new Colour("black", "#3A3A3A")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 1599),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 1269),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 1599),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 1599)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(128, 0),
                new SpecsOption(256, 100)
        ));
        specs = new PhoneSpecs("OnePlus 10 Pro",
                "Android 12",
                "17.02 centimeters (6.7 inches) (measured diagonally from corner to corner); 3216 X 1440 pixels 525 ppi; 120 Hz Fluid AMOLED with LTPO",
                "Snapdragon® 8 Gen 1 Mobile Platform",
                storages,
                "Main Camera; Ultra-Wide Camera; Telephoto Camera",
                "5G; LTE; Wi-Fi 802.11a/b/g/n/ac/ax; Bluetooth v5.2",
                "Dual Stereo Speakers; Noise cancellation support; Dolby Atmos",
                "Yes",
                "IP68",
                "Dual nano-SIM slot",
                "In-display Fingerprint Sensor; Accelerometer; Electronic Compass; Gyroscope; Ambient Light Sensor; Proximity Sensor; Sensor Core; Flicker-detect Sensor; Front RGB sensor",
                "5000mAh (2S1P 2,500 mAh, non-removable)",
                "73.9 x 163.0 x 8.55mm",
                "200.5g");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_phone_oneplus_10_pro_1_emerald",
                "item_phone_oneplus_10_pro_2_emerald",
                "item_phone_oneplus_10_pro_3_emerald",

                "item_phone_oneplus_10_pro_1_black",
                "item_phone_oneplus_10_pro_2_black",
                "item_phone_oneplus_10_pro_3_black"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Phone("OnePlus 10 Pro",
                "OnePlus",
                (PhoneSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Phone 7
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("silver", "#6A4E57"),
                new Colour("blue", "#000000")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 1075.99),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 1184),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 1075.99)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(128, 0),
                new SpecsOption(256, 100),
                new SpecsOption(512, 400)
        ));
        specs = new PhoneSpecs("HUAWEI P40 Pro",
                "EMUI 10.1(Based on Android 10)",
                "6.58 inches; OLED, up to 90 Hz frame refresh rate; 2640 x 1200 Pixels",
                "HUAWEI Kirin 990 5G; Octa-core",
                storages,
                "Rear Camera; 50 MP Ultra Vision Camera + 40 MP Cine Camera + 3D Depth Sensing Camera",
                "5G; LTE; Wi-Fi 802.11; Bluetooth v5.2",
                "Stereo speakers; Audio playback format",
                "Yes",
                "IP68",
                "Dual SIM model",
                "Gesture Sensor; Gravity Sensor; Infrared Sensor; Fingerprint Sensor; Hall Sensor; Gyroscope; Compass; Ambient Light Sensor; Proximity Sensor; Colour Temperature Sensor",
                "4200 mAh",
                "72.6 x 158.2 x 8.95mm",
                "209g");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_phone_huawei_p40_pro_1_silver",
                "item_phone_huawei_p40_pro_2_silver",
                "item_phone_huawei_p40_pro_3_silver",

                "item_phone_huawei_p40_pro_1_blue",
                "item_phone_huawei_p40_pro_2_blue",
                "item_phone_huawei_p40_pro_3_blue"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Phone("HUAWEI P40 Pro",
                "HUAWEI",
                (PhoneSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Phone 8
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("blue", "#A8C9DE"),
                new Colour("purple", "#E2D0E4"),
                new Colour("grey", "#525964")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 1149),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 1129),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 1129),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 1149)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(256, 0)
        ));
        specs = new PhoneSpecs("Xiaomi 12 Pro",
                "Android 12",
                "WQHD+ 6.73\" AMOLED DotDisplay; 3200 x 1440; 552ppi; 480Hz",
                "Snapdragon® 8 Gen 1",
                storages,
                "Pro-grade 50MP triple camera array; 50MP wide angle camera + 50MP ultra-wide angle camera + 50MP telephoto camera; 32MP in-display selfie camera",
                "5G; LTE; Wi-Fi 6 / Wi-Fi 6E; Bluetooth v5.2",
                "Quad speakers; Dolby Atmos®; SOUND BY Harman Kardon",
                "Yes",
                "IP68",
                "Dual SIM model",
                "In-screen fingerprint sensor; AI face unlock; Proximity sensor | Ambient light sensor | Accelerometer | Gyroscope | Electronic compass | Linear motor | IR blaster | Barometer | Flicker sensor",
                "4600mAh",
                "74.6 x 163.6 x 8.16mm",
                "205g");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_phone_xiaomi_12_pro_1_blue",
                "item_phone_xiaomi_12_pro_2_blue",
                "item_phone_xiaomi_12_pro_3_blue",

                "item_phone_xiaomi_12_pro_1_purple",
                "item_phone_xiaomi_12_pro_2_purple",
                "item_phone_xiaomi_12_pro_3_purple",

                "item_phone_xiaomi_12_pro_1_grey",
                "item_phone_xiaomi_12_pro_2_grey",
                "item_phone_xiaomi_12_pro_3_grey"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Phone("Xiaomi 12 Pro",
                "Xiaomi",
                (PhoneSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Phone 9
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("burgundy", "#6A4E57"),
                new Colour("black", "#000000"),
                new Colour("white", "#FFFFFF"),
                new Colour("green", "#507974")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 459),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 499),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 449),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 459)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(128, 0),
                new SpecsOption(256, 100),
                new SpecsOption(512, 400)
        ));
        specs = new PhoneSpecs("Samsung Galaxy S22 Ultra 5G Dual SIM Smartphone 8GB+128GB - Black (Wall Charger & Headset sold separately)",
                "Android 12",
                "6.8\" edge Quad HD+ Dynamic AMOLED 2X; Infinity-O Display (3088x1440); 120Hz refresh rate",
                "Snapdragon 8 Gen 1 4nm octa-core flagship processor",
                storages,
                "Quad Rear Camera; 108MP Main sensor, F1.8, OIS; 12MP Ultra Wide angle, F2.2; 10MP 3x Telephoto, F2.4, OIS; 10MP 10x Periscope Telephoto, F4.9, OIS; Up to 100x Space Zoom; 40MP Front camera, F2.2",
                "5G; LTE; Wi-Fi 802.11; Bluetooth v5.2",
                "Stereo speakers; Ultra high quality audio playback; Audio playback format",
                "Yes",
                "IP68",
                "Dual SIM model",
                "Ultrasonic Fingerprint sensor; Geomagnetic sensor; Accelerometer; Hall sensor; Barometer; Proximity sensor; Gyro sensor; Ambient Light sensor",
                "5000mAh",
                "77.9 x 163.3 x 8.9mm",
                "228g");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_phone_samsung_galaxy_s22_ultra_1_burgundy",
                "item_phone_samsung_galaxy_s22_ultra_2_burgundy",
                "item_phone_samsung_galaxy_s22_ultra_3_burgundy",

                "item_phone_samsung_galaxy_s22_ultra_1_black",
                "item_phone_samsung_galaxy_s22_ultra_2_black",
                "item_phone_samsung_galaxy_s22_ultra_3_black",

                "item_phone_samsung_galaxy_s22_ultra_1_white",
                "item_phone_samsung_galaxy_s22_ultra_2_white",
                "item_phone_samsung_galaxy_s22_ultra_3_white",

                "item_phone_samsung_galaxy_s22_ultra_1_green",
                "item_phone_samsung_galaxy_s22_ultra_2_green",
                "item_phone_samsung_galaxy_s22_ultra_3_green"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Phone("Samsung Galaxy S22 Ultra",
                "Samsung",
                (PhoneSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Phone 10
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("burgundy", "#6A4E57"),
                new Colour("black", "#000000"),
                new Colour("white", "#FFFFFF"),
                new Colour("green", "#507974")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 459),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 499),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 449),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 459)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(128, 0),
                new SpecsOption(256, 100),
                new SpecsOption(512, 400)
        ));
        specs = new PhoneSpecs("Samsung Galaxy S22 Ultra 5G Dual SIM Smartphone 8GB+128GB - Black (Wall Charger & Headset sold separately)",
                "Android 12",
                "6.8\" edge Quad HD+ Dynamic AMOLED 2X; Infinity-O Display (3088x1440); 120Hz refresh rate",
                "Snapdragon 8 Gen 1 4nm octa-core flagship processor",
                storages,
                "Quad Rear Camera; 108MP Main sensor, F1.8, OIS; 12MP Ultra Wide angle, F2.2; 10MP 3x Telephoto, F2.4, OIS; 10MP 10x Periscope Telephoto, F4.9, OIS; Up to 100x Space Zoom; 40MP Front camera, F2.2",
                "5G; LTE; Wi-Fi 802.11; Bluetooth v5.2",
                "Stereo speakers; Ultra high quality audio playback; Audio playback format",
                "Yes",
                "IP68",
                "Dual SIM model",
                "Ultrasonic Fingerprint sensor; Geomagnetic sensor; Accelerometer; Hall sensor; Barometer; Proximity sensor; Gyro sensor; Ambient Light sensor",
                "5000mAh",
                "77.9 x 163.3 x 8.9mm",
                "228g");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_phone_samsung_galaxy_s22_ultra_1_burgundy",
                "item_phone_samsung_galaxy_s22_ultra_2_burgundy",
                "item_phone_samsung_galaxy_s22_ultra_3_burgundy",

                "item_phone_samsung_galaxy_s22_ultra_1_black",
                "item_phone_samsung_galaxy_s22_ultra_2_black",
                "item_phone_samsung_galaxy_s22_ultra_3_black",

                "item_phone_samsung_galaxy_s22_ultra_1_white",
                "item_phone_samsung_galaxy_s22_ultra_2_white",
                "item_phone_samsung_galaxy_s22_ultra_3_white",

                "item_phone_samsung_galaxy_s22_ultra_1_green",
                "item_phone_samsung_galaxy_s22_ultra_2_green",
                "item_phone_samsung_galaxy_s22_ultra_3_green"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                // TODO: need to add accessories first
        ));
        items.add(new Phone("Samsung Galaxy S22 Ultra",
                "Samsung",
                (PhoneSpecs) specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));


        /**
         * Accessories
         */
        /*
         * Accessory 1
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000"),
                new Colour("silver", "#C0C0C0")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 459),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 499),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 449),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 459)
        ));
        specs = new AccessorySpecs("Sony WH-1000XM4 Wireless Over-Ear Noise-Cancelling Headphones");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_sony_wh_1000xm4_1_black",
                "item_accessory_sony_wh_1000xm4_2_black",
                "item_accessory_sony_wh_1000xm4_3_black",

                "item_accessory_sony_wh_1000xm4_1_silver",
                "item_accessory_sony_wh_1000xm4_2_silver",
                "item_accessory_sony_wh_1000xm4_3_silver"
        ));
        items.add(new Accessory("Sony WH-1000XM4",
                "Sony",
                (AccessorySpecs) specs,
                itemStoreVariant,
                imageUris));

        /*
         * Accessory 2
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000"),
                new Colour("silver", "#C0C0C0")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 459),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 499),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 449),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 459)
        ));
        specs = new AccessorySpecs("Sony WH-1000XM4 Wireless Over-Ear Noise-Cancelling Headphones");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_sony_wh_1000xm4_1_black",
                "item_accessory_sony_wh_1000xm4_2_black",
                "item_accessory_sony_wh_1000xm4_3_black",

                "item_accessory_sony_wh_1000xm4_1_silver",
                "item_accessory_sony_wh_1000xm4_2_silver",
                "item_accessory_sony_wh_1000xm4_3_silver"
        ));
        items.add(new Accessory("Sony WH-1000XM4",
                "Sony",
                (AccessorySpecs) specs,
                itemStoreVariant,
                imageUris));

        /*
         * Accessory 3
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000"),
                new Colour("silver", "#C0C0C0")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 459),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 499),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 449),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 459)
        ));
        specs = new AccessorySpecs("Sony WH-1000XM4 Wireless Over-Ear Noise-Cancelling Headphones");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_sony_wh_1000xm4_1_black",
                "item_accessory_sony_wh_1000xm4_2_black",
                "item_accessory_sony_wh_1000xm4_3_black",

                "item_accessory_sony_wh_1000xm4_1_silver",
                "item_accessory_sony_wh_1000xm4_2_silver",
                "item_accessory_sony_wh_1000xm4_3_silver"
        ));
        items.add(new Accessory("Sony WH-1000XM4",
                "Sony",
                (AccessorySpecs) specs,
                itemStoreVariant,
                imageUris));

        /*
         * Accessory 4
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000"),
                new Colour("silver", "#C0C0C0")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 459),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 499),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 449),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 459)
        ));
        specs = new AccessorySpecs("Sony WH-1000XM4 Wireless Over-Ear Noise-Cancelling Headphones");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_sony_wh_1000xm4_1_black",
                "item_accessory_sony_wh_1000xm4_2_black",
                "item_accessory_sony_wh_1000xm4_3_black",

                "item_accessory_sony_wh_1000xm4_1_silver",
                "item_accessory_sony_wh_1000xm4_2_silver",
                "item_accessory_sony_wh_1000xm4_3_silver"
        ));
        items.add(new Accessory("Sony WH-1000XM4",
                "Sony",
                (AccessorySpecs) specs,
                itemStoreVariant,
                imageUris));

        /*
         * Accessory 5
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000"),
                new Colour("silver", "#C0C0C0")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 459),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 499),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 449),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 459)
        ));
        specs = new AccessorySpecs("Sony WH-1000XM4 Wireless Over-Ear Noise-Cancelling Headphones");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_sony_wh_1000xm4_1_black",
                "item_accessory_sony_wh_1000xm4_2_black",
                "item_accessory_sony_wh_1000xm4_3_black",

                "item_accessory_sony_wh_1000xm4_1_silver",
                "item_accessory_sony_wh_1000xm4_2_silver",
                "item_accessory_sony_wh_1000xm4_3_silver"
        ));
        items.add(new Accessory("Sony WH-1000XM4",
                "Sony",
                (AccessorySpecs) specs,
                itemStoreVariant,
                imageUris));

        /*
         * Accessory 6
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000"),
                new Colour("silver", "#C0C0C0")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 459),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 499),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 449),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 459)
        ));
        specs = new AccessorySpecs("Sony WH-1000XM4 Wireless Over-Ear Noise-Cancelling Headphones");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_sony_wh_1000xm4_1_black",
                "item_accessory_sony_wh_1000xm4_2_black",
                "item_accessory_sony_wh_1000xm4_3_black",

                "item_accessory_sony_wh_1000xm4_1_silver",
                "item_accessory_sony_wh_1000xm4_2_silver",
                "item_accessory_sony_wh_1000xm4_3_silver"
        ));
        items.add(new Accessory("Sony WH-1000XM4",
                "Sony",
                (AccessorySpecs) specs,
                itemStoreVariant,
                imageUris));

        /*
         * Accessory 7
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000"),
                new Colour("silver", "#C0C0C0")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 459),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 499),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 449),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 459)
        ));
        specs = new AccessorySpecs("Sony WH-1000XM4 Wireless Over-Ear Noise-Cancelling Headphones");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_sony_wh_1000xm4_1_black",
                "item_accessory_sony_wh_1000xm4_2_black",
                "item_accessory_sony_wh_1000xm4_3_black",

                "item_accessory_sony_wh_1000xm4_1_silver",
                "item_accessory_sony_wh_1000xm4_2_silver",
                "item_accessory_sony_wh_1000xm4_3_silver"
        ));
        items.add(new Accessory("Sony WH-1000XM4",
                "Sony",
                (AccessorySpecs) specs,
                itemStoreVariant,
                imageUris));

        /*
         * Accessory 8
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000"),
                new Colour("silver", "#C0C0C0")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 459),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 499),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 449),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 459)
        ));
        specs = new AccessorySpecs("Sony WH-1000XM4 Wireless Over-Ear Noise-Cancelling Headphones");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_sony_wh_1000xm4_1_black",
                "item_accessory_sony_wh_1000xm4_2_black",
                "item_accessory_sony_wh_1000xm4_3_black",

                "item_accessory_sony_wh_1000xm4_1_silver",
                "item_accessory_sony_wh_1000xm4_2_silver",
                "item_accessory_sony_wh_1000xm4_3_silver"
        ));
        items.add(new Accessory("Sony WH-1000XM4",
                "Sony",
                (AccessorySpecs) specs,
                itemStoreVariant,
                imageUris));

        /*
         * Accessory 9
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000"),
                new Colour("silver", "#C0C0C0")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 459),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 499),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 449),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 459)
        ));
        specs = new AccessorySpecs("Sony WH-1000XM4 Wireless Over-Ear Noise-Cancelling Headphones");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_sony_wh_1000xm4_1_black",
                "item_accessory_sony_wh_1000xm4_2_black",
                "item_accessory_sony_wh_1000xm4_3_black",

                "item_accessory_sony_wh_1000xm4_1_silver",
                "item_accessory_sony_wh_1000xm4_2_silver",
                "item_accessory_sony_wh_1000xm4_3_silver"
        ));
        items.add(new Accessory("Sony WH-1000XM4",
                "Sony",
                (AccessorySpecs) specs,
                itemStoreVariant,
                imageUris));

        /*
         * Accessory 10
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000"),
                new Colour("silver", "#C0C0C0")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 459),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 499),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 449),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 459)
        ));
        specs = new AccessorySpecs("Sony WH-1000XM4 Wireless Over-Ear Noise-Cancelling Headphones");
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_sony_wh_1000xm4_1_black",
                "item_accessory_sony_wh_1000xm4_2_black",
                "item_accessory_sony_wh_1000xm4_3_black",

                "item_accessory_sony_wh_1000xm4_1_silver",
                "item_accessory_sony_wh_1000xm4_2_silver",
                "item_accessory_sony_wh_1000xm4_3_silver"
        ));
        items.add(new Accessory("Sony WH-1000XM4",
                "Sony",
                (AccessorySpecs) specs,
                itemStoreVariant,
                imageUris));

        return items;
    }

    public static void addItemsToFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<IItem> items = generateItems();
        String collectionPath;

        for (IItem item : items) {
            switch (item.getCategoryType()) {
                case laptops:
                    collectionPath = CollectionPath.laptops.name();
                    break;
                case phones:
                    collectionPath = CollectionPath.phones.name();
                    break;
                default:
                    collectionPath = CollectionPath.accessories.name();
                    break;
            }

            db.collection(collectionPath).add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.i("Add items to Firebase", item + " added.");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("Add items to Firebase", item + " NOT added.");
                }
            });
        }
    }
}
