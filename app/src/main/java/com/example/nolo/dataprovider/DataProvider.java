package com.example.nolo.dataprovider;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.category.Category;
import com.example.nolo.entities.category.ICategory;
import com.example.nolo.entities.item.Accessory;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.Laptop;
import com.example.nolo.entities.item.Phone;
import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.entities.item.specs.Specs;
import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.entities.store.Branch;
import com.example.nolo.entities.store.IStore;
import com.example.nolo.entities.store.Store;
import com.example.nolo.entities.user.IUser;
import com.example.nolo.entities.user.User;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.enums.CollectionPath;
import com.example.nolo.enums.SpecsOptionType;
import com.example.nolo.enums.SpecsType;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     *
     * @param path
     * @param addEntityMethod
     */
    public static void clearAndAddEntity(String path, Consumer<Void> addEntityMethod) {
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

    public static void addCategoriesToFirebase() {
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
        List<ItemVariant> history = new ArrayList<>();
        List<Purchasable> cart = new ArrayList<>();
        List<Purchasable> purchase = new ArrayList<>();
        IUser u;

        history.add(new ItemVariant());
        cart.add(new Purchasable());
        u = new User(history, cart, purchase);
        u.setEmail("john.bm.chen@gmail.com");
        users.add(u);

        u = new User(history, cart, purchase);
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
     * ITEMS.LAPTOPS
     */
    private static List<IItem> generateLaptops() {
        List<IItem> items = new ArrayList<>();
        List<String> imageUris, recommendedAccessoryIds;
        Map<String, String> fixedSpecs;
        Map<String, List<SpecsOption>> customisableSpecs;
        Specs specs;
        List<SpecsOption> rams, storages;
        List<StoreVariant> itemStoreVariant;
        List<Colour> colours;

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
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "MacBook Air M2 chip model"},
                {SpecsType.operatingSystem.name(), "macOS"},
                {SpecsType.display.name(), "13.6-inch (diagonal) LED-backlit display with IPS technology; (2560*1664), Liquid Retina display"},
                {SpecsType.cpu.name(), "Apple M2 chip; 8-core CPU with four performance cores and four efficiency cores"},
                {SpecsType.gpu.name(), "Integrated Graphics 8-core GPU"},
                {SpecsType.camera.name(), "1080p FaceTime HD camera"},
                {SpecsType.keyboard.name(), "Backlit Magic Keyboard"},
                {SpecsType.communication.name(), "802.11ax Wi-Fi 6 wireless networking + Bluetooth 5.0 wireless technology"},
                {SpecsType.audio.name(), "Four-speaker sound system; Wide stereo sound"},
                {SpecsType.touchscreen.name(), "N/A"},
                {SpecsType.fingerprintReader.name(), "Touch ID"},
                {SpecsType.opticalDrive.name(), "N/A"},
                {SpecsType.ports.name(), "Thunderbolt 3 digital video output; Native DisplayPort output over USB‑C"},
                {SpecsType.battery.name(), "52.6-watt‑hour lithium‑polymer battery"},
                {SpecsType.acAdaptor.name(), "30W USB-C Power Adapter"},
                {SpecsType.dimensions.name(), "304.1 x 215 x 11.3 mm"},
                {SpecsType.weight.name(), "1.24 kg"}
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.ram.name(), rams);
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
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
                "Sony WH-1000XM4",
                "Apple AirPods Pro",
                "BOOM 3 Bluetooth Speaker"
        ));
        items.add(new Laptop("Apple MacBook Air (M2)",
                "Apple",
                specs,
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
                new SpecsOption(4096, 2100)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "MacBook Pro 16\" models"},
                {SpecsType.operatingSystem.name(), "macOS"},
                {SpecsType.display.name(), "16.2-inch (diagonal) Liquid Retina XDR display, (3456*2234)"},
                {SpecsType.cpu.name(), "Apple M1 Pro chip; 10-core CPU with eight performance cores and two efficiency cores"},
                {SpecsType.gpu.name(), "Integrated Graphics 16-core GPU"},
                {SpecsType.camera.name(), "1080p FaceTime HD camera"},
                {SpecsType.keyboard.name(), "Backlit Magic Keyboard"},
                {SpecsType.communication.name(), "802.11ax Wi-Fi 6 wireless networking + Bluetooth 5.0 wireless technology"},
                {SpecsType.audio.name(), "High-fidelity six-speaker sound system with force-cancelling woofers; Wide stereo sound"},
                {SpecsType.touchscreen.name(), "N/A"},
                {SpecsType.fingerprintReader.name(), "Touch ID"},
                {SpecsType.opticalDrive.name(), "N/A"},
                {SpecsType.ports.name(), "SDXC card slot; HDMI port; 3.5-mm headphone jack; MagSafe 3 port; Three Thunderbolt 4 (USB-C) ports"},
                {SpecsType.battery.name(), "100-watt-hour lithium-polymer battery"},
                {SpecsType.acAdaptor.name(), "140W USB-C Power Adapter"},
                {SpecsType.dimensions.name(), "355.7 x 248.1 x 16.8 mm"},
                {SpecsType.weight.name(), "2.15 kg"}
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.ram.name(), rams);
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_macbook_pro_16_1_silver",
                "item_laptop_macbook_pro_16_2_silver",
                "item_laptop_macbook_pro_16_3_silver",

                "item_laptop_macbook_pro_16_1_grey",
                "item_laptop_macbook_pro_16_2_grey",
                "item_laptop_macbook_pro_16_3_grey"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                "Sony WH-1000XM4",
                "Apple AirPods Pro",
                "BOOM 3 Bluetooth Speaker"
        ));
        items.add(new Laptop("Apple MacBook Pro 16\"",
                "Apple",
                specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Laptop 3
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000"),
                new Colour("blue", "#9A9CA3"),
                new Colour("sandstone", "#CBB1A0"),
                new Colour("platinum", "#79838A")
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
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Microsoft Surface Laptop 4 13.5\""},
                {SpecsType.operatingSystem.name(), "Windows 11 Home"},
                {SpecsType.display.name(), "13.5\" 2256 x 1504 (201 PPI)"},
                {SpecsType.cpu.name(), "AMD Ryzen™ 5 4680U Mobile Processor with Radeon™ Graphics Microsoft Surface® Edition (6 cores)"},
                {SpecsType.gpu.name(), "AMD Ryzen™ Microsoft Surface® Edition: AMD Radeon™ Graphics"},
                {SpecsType.camera.name(), "Windows Hello face authentication camera (front-facing); 720p HD f2.0 camera (front-facing)"},
                {SpecsType.keyboard.name(), "Backlight"},
                {SpecsType.communication.name(), "Wi-Fi 6: 802.11ax + Bluetooth® Wireless 5.0 technology"},
                {SpecsType.audio.name(), "Dual far-field Studio Mics; Omnisonic Speakers with Dolby Atmos"},
                {SpecsType.touchscreen.name(), "10 point multi-touch"},
                {SpecsType.fingerprintReader.name(), "N/A"},
                {SpecsType.opticalDrive.name(), "N/A"},
                {SpecsType.ports.name(), "1 x USB-C; 1 x USB-A; 3.5 mm headphone jack; 1 x Surface Connect port"},
                {SpecsType.battery.name(), "Battery Capacity Nominal (WH) 47.4"},
                {SpecsType.acAdaptor.name(), "Power Supply"},
                {SpecsType.dimensions.name(), "308 x 223 x 14.5 mm"},
                {SpecsType.weight.name(), "1.27 kg"}
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.ram.name(), rams);
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
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
                "Sony WH-1000XM4",
                "BOOM 3 Bluetooth Speaker",
                "Logitech Pro X DTS Headphone"
        ));
        items.add(new Laptop("Microsoft Surface Laptop 4",
                "Microsoft",
                specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Laptop 3.5
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000"),
                new Colour("blue", "#9A9CA3"),
                new Colour("sandstone", "#CBB1A0"),
                new Colour("platinum", "#79838A")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 1549),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 1599),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 1598)
        ));
        rams = new ArrayList<>(Arrays.asList(
                new SpecsOption(8, 0),
                new SpecsOption(16, 400)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(256, 0),
                new SpecsOption(512, 450)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Microsoft Surface Laptop 3 13.5\""},
                {SpecsType.operatingSystem.name(), "Windows 11 Home"},
                {SpecsType.display.name(), "13.5\" 2256 x 1504 (201 PPI)"},
                {SpecsType.cpu.name(), "AMD Ryzen™ 5 4680U Mobile Processor with Radeon™ Graphics Microsoft Surface® Edition (6 cores)"},
                {SpecsType.gpu.name(), "AMD Ryzen™ Microsoft Surface® Edition: AMD Radeon™ Graphics"},
                {SpecsType.camera.name(), "Windows Hello face authentication camera (front-facing); 720p HD f2.0 camera (front-facing)"},
                {SpecsType.keyboard.name(), "Backlight"},
                {SpecsType.communication.name(), "Wi-Fi 6: 802.11ax + Bluetooth® Wireless 5.0 technology"},
                {SpecsType.audio.name(), "Dual far-field Studio Mics; Omnisonic Speakers with Dolby Atmos"},
                {SpecsType.touchscreen.name(), "10 point multi-touch"},
                {SpecsType.fingerprintReader.name(), "N/A"},
                {SpecsType.opticalDrive.name(), "N/A"},
                {SpecsType.ports.name(), "1 x USB-C; 1 x USB-A; 3.5 mm headphone jack; 1 x Surface Connect port"},
                {SpecsType.battery.name(), "Battery Capacity Nominal (WH) 47.4"},
                {SpecsType.acAdaptor.name(), "Power Supply"},
                {SpecsType.dimensions.name(), "308 x 223 x 14.5 mm"},
                {SpecsType.weight.name(), "1.27 kg"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.ram.name(), rams);
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
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
                "Sony WH-1000XM4",
                "BOOM 3 Bluetooth Speaker",
                "Logitech Pro X DTS Headphone"
        ));
        items.add(new Laptop("Microsoft Surface Laptop 3",
                "Microsoft",
                specs,
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
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Microsoft Surface Pro 8"},
                {SpecsType.operatingSystem.name(), "Windows 11 Home"},
                {SpecsType.display.name(), "13\" 2880 x 1920 (267 PPI) 120Hz"},
                {SpecsType.cpu.name(), "Quad-core 11th Gen Intel® Core™ i5-1135G7 Processor, designed on the Intel® Evo™ platform"},
                {SpecsType.gpu.name(), "Integrated Graphics"},
                {SpecsType.camera.name(), "Windows Hello face authentication camera (front-facing); 5.0MP front-facing camera with 1080p full HD video; 10.0MP rear-facing autofocus camera with 1080p HD and 4k video"},
                {SpecsType.keyboard.name(), "Backlight"},
                {SpecsType.communication.name(), "Wi-Fi 6: 802.11ax + Bluetooth® Wireless 5.1 technology"},
                {SpecsType.audio.name(), "Dual far-field Studio Mics; 2W stereo speakers with Dolby Atmos"},
                {SpecsType.touchscreen.name(), "10 point multi-touch"},
                {SpecsType.fingerprintReader.name(), "N/A"},
                {SpecsType.opticalDrive.name(), "N/A"},
                {SpecsType.ports.name(), "2 x USB-C® with USB 4.0/Thunderbolt™ 4; 3.5mm headphone jack; 1 × Surface Connect port; Surface Type Cover port"},
                {SpecsType.battery.name(), "Battery Capacity Nominal (WH) 51.5Wh"},
                {SpecsType.acAdaptor.name(), "Power Supply"},
                {SpecsType.dimensions.name(), "287 x 208 x 9.3 mm"},
                {SpecsType.weight.name(), "891 g"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.ram.name(), rams);
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_surface_pro_8_1_platinum",
                "item_laptop_surface_pro_8_2_platinum",
                "item_laptop_surface_pro_8_3_platinum",

                "item_laptop_surface_pro_8_1_graphite",
                "item_laptop_surface_pro_8_2_graphite",
                "item_laptop_surface_pro_8_3_graphite"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                "Sony WH-1000XM4",
                "BOOM 3 Bluetooth Speaker",
                "Logitech Pro X DTS Headphone"
        ));
        items.add(new Laptop("Microsoft Surface Pro 8",
                "Microsoft",
                specs,
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
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "ASUS Vivobook Go 14 Flip TP1400KA Laptop 14\" HD Touch"},
                {SpecsType.operatingSystem.name(), "Windows 11 Home"},
                {SpecsType.display.name(), "14.0-inch, FHD (1920 x 1080) 16:9 aspect ratio"},
                {SpecsType.cpu.name(), "Intel® Celeron® N4500 Processor 1.1 GHz (4M Cache, up to 2.8 GHz, 2 cores)"},
                {SpecsType.gpu.name(), "Intel® UHD Graphics"},
                {SpecsType.camera.name(), "720p HD camera"},
                {SpecsType.keyboard.name(), "Backlit Chiclet Keyboard; 1.4mm Key-travel"},
                {SpecsType.communication.name(), "Wi-Fi 5(802.11ac) (Dual band) 1*1 + Bluetooth 4.1"},
                {SpecsType.audio.name(), "SonicMaster; Built-in speaker; Built-in array microphone with Cortana support"},
                {SpecsType.touchscreen.name(), "Touch screen display"},
                {SpecsType.fingerprintReader.name(), "Fingerprint sensor integrated with Touchpad"},
                {SpecsType.opticalDrive.name(), "N/A"},
                {SpecsType.ports.name(), "1x USB 2.0 Type-A; 1x USB 3.2 Gen 1 Type-A; 1x USB 3.2 Gen 1 Type-C; 1x HDMI 1.4; 1x 3.5mm Combo Audio Jack; 1x DC-in; Micro SD card reader"},
                {SpecsType.battery.name(), "39WHrs, 2S1P, 2-cell Li-ion"},
                {SpecsType.acAdaptor.name(), "45W AC Adapter"},
                {SpecsType.dimensions.name(), "32.07 x 21.70 x 1.69 mm"},
                {SpecsType.weight.name(), "1.50 kg"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.ram.name(), rams);
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_asus_vivobook_go_14_flip_tp1400ka_1_blue",
                "item_laptop_asus_vivobook_go_14_flip_tp1400ka_2_blue",
                "item_laptop_asus_vivobook_go_14_flip_tp1400ka_3_blue"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                "Sony WH-1000XM4",
                "BOOM 3 Bluetooth Speaker",
                "Logitech Pro X DTS Headphone"
        ));
        items.add(new Laptop("ASUS Vivobook Go 14 Flip TP1400KA",
                "ASUS",
                specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Laptop 5.5
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 2540.99),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 2499),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 2527.99)
        ));
        rams = new ArrayList<>(Arrays.asList(
                new SpecsOption(4, 0),
                new SpecsOption(8, 59)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(256, 0),
                new SpecsOption(512, 99)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "ASUS ROG Strix G15 Advantage Edition G513QY"},
                {SpecsType.operatingSystem.name(), "Windows 11 Home"},
                {SpecsType.display.name(), "14.0-inch, FHD (1920 x 1080) 16:9 aspect ratio"},
                {SpecsType.cpu.name(), "Intel® Celeron® N4500 Processor 1.1 GHz (4M Cache, up to 2.8 GHz, 2 cores)"},
                {SpecsType.gpu.name(), "Intel® UHD Graphics"},
                {SpecsType.camera.name(), "720p HD camera"},
                {SpecsType.keyboard.name(), "Backlit Chiclet Keyboard; 1.4mm Key-travel"},
                {SpecsType.communication.name(), "Wi-Fi 5(802.11ac) (Dual band) 1*1 + Bluetooth 4.1"},
                {SpecsType.audio.name(), "SonicMaster; Built-in speaker; Built-in array microphone with Cortana support"},
                {SpecsType.touchscreen.name(), "Touch screen display"},
                {SpecsType.fingerprintReader.name(), "Fingerprint sensor integrated with Touchpad"},
                {SpecsType.opticalDrive.name(), "N/A"},
                {SpecsType.ports.name(), "1x USB 2.0 Type-A; 1x USB 3.2 Gen 1 Type-A; 1x USB 3.2 Gen 1 Type-C; 1x HDMI 1.4; 1x 3.5mm Combo Audio Jack; 1x DC-in; Micro SD card reader"},
                {SpecsType.battery.name(), "39WHrs, 2S1P, 2-cell Li-ion"},
                {SpecsType.acAdaptor.name(), "45W AC Adapter"},
                {SpecsType.dimensions.name(), "32.07 x 21.70 x 1.69 mm"},
                {SpecsType.weight.name(), "1.50 kg"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.ram.name(), rams);
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_asus_rog_strix_g15_1_black",
                "item_laptop_asus_rog_strix_g15_2_black",
                "item_laptop_asus_rog_strix_g15_3_black"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                "Sony WH-1000XM4",
                "BOOM 3 Bluetooth Speaker",
                "Logitech Pro X DTS Headphone"
        ));
        items.add(new Laptop("ASUS ROG Strix G15 Advantage Edition",
                "ASUS",
                specs,
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
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 2362.39),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 2462.39),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 2522.39)
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
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Lenovo ThinkPad X1 Carbon Gen 10 - Intel® Evo"},
                {SpecsType.operatingSystem.name(), "Windows 11 Home"},
                {SpecsType.display.name(), "14\" WUXGA (1920 x 1200); IPS; Anti-Glare; Non-Touch; 100%sRGB; 400 nits; Narrow Bezel; Low Blue Light"},
                {SpecsType.cpu.name(), "12th Generation Intel® Core™ i5-1240P Processor (E-cores up to 3.30 GHz P-cores up to 4.40 GHz)"},
                {SpecsType.gpu.name(), "Integrated Intel® Iris® Xe Graphics"},
                {SpecsType.camera.name(), "FHD IR/RGB Hybrid with Microphone"},
                {SpecsType.keyboard.name(), "Backlit; Black with Fingerprint Reader - English"},
                {SpecsType.communication.name(), "Wi-Fi 6E AX211 2x2 AX & Bluetooth® 5.0"},
                {SpecsType.audio.name(), "Audio jack; Two upward- and two downward-firing speakers"},
                {SpecsType.touchscreen.name(), "N/A"},
                {SpecsType.fingerprintReader.name(), "Fingerprint Reader"},
                {SpecsType.opticalDrive.name(), "N/A"},
                {SpecsType.ports.name(), "2x USB-C Thunderbolt 4 ports; 2x USB 3.2 Type-A port; 1x HDMI video output; 1x nano SIM slot"},
                {SpecsType.battery.name(), "4 Cell Li-Polymer Internal Battery, 57Wh"},
                {SpecsType.acAdaptor.name(), "65W adapter"},
                {SpecsType.dimensions.name(), "323.5 x 217.1 x 15.95 mm"},
                {SpecsType.weight.name(), "1.13 kg"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.ram.name(), rams);
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_lenovo_thinkpad_x1_1_black",
                "item_laptop_lenovo_thinkpad_x1_2_black",
                "item_laptop_lenovo_thinkpad_x1_3_black"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                "Sony WH-1000XM4",
                "BOOM 3 Bluetooth Speaker",
                "Logitech Pro X DTS Headphone"
        ));
        items.add(new Laptop("Lenovo ThinkPad X1 Carbon Gen 10 - Intel® Evo",
                "Lenovo",
                specs,
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
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Legion 5 Pro (16\", Gen 7) AMD"},
                {SpecsType.operatingSystem.name(), "Windows 11 Home"},
                {SpecsType.display.name(), "16.0\" WQXGA (2560x1600) IPS 500nits Anti-glare; 165Hz"},
                {SpecsType.cpu.name(), "AMD Ryzen™ 7 6800H"},
                {SpecsType.gpu.name(), "NVIDIA® GeForce RTX™ 3050 Ti 4GB GDDR6"},
                {SpecsType.camera.name(), "HD 720p, with E-camera shutter, fixed focus"},
                {SpecsType.keyboard.name(), "6-row, multimedia Fn keys, numeric keypad, black keycap"},
                {SpecsType.communication.name(), "802.11AX (2x2) & Bluetooth® 5.1"},
                {SpecsType.audio.name(), "Stereo speakers; 2 x 2W, Nahimic Audio; Dual array mic"},
                {SpecsType.touchscreen.name(), "Non-touch"},
                {SpecsType.fingerprintReader.name(), "No fingerprint reader"},
                {SpecsType.opticalDrive.name(), "N/A"},
                {SpecsType.ports.name(), "2 x USB 3.2 Gen 1; 1 x USB 3.2 Gen 1 (Always On); 2 x USB-C 3.2 Gen 2 (support data transfer and DisplayPort™ 1.4); 1 x USB-C 3.2 Gen 2 (support data transfer; Power Delivery 135W and DisplayPort 1.4); 1 x HDMI 2.1; 1 x Ethernet (RJ-45); 1 x Headphone / mic; 1 x Power connector"},
                {SpecsType.battery.name(), "4-cell (80Wh), integrated"},
                {SpecsType.acAdaptor.name(), "230W Slim Tip (3-pin)"},
                {SpecsType.dimensions.name(), "359.9 x 262.4 x 19.9 mm"},
                {SpecsType.weight.name(), "2.49 kg"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.ram.name(), rams);
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_legion_5_pro_16arh7_1_grey",
                "item_laptop_legion_5_pro_16arh7_2_grey",
                "item_laptop_legion_5_pro_16arh7_3_grey",

                "item_laptop_legion_5_pro_16arh7_1_white",
                "item_laptop_legion_5_pro_16arh7_2_white",
                "item_laptop_legion_5_pro_16arh7_3_white"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                "Logitech Pro X DTS Headphone",
                "HyperX QuadCast S Standalone Microphone",
                "Sony WH-1000XM4"
        ));
        items.add(new Laptop("Lenovo Legion 5 Pro 16ARH7",
                "Lenovo",
                specs,
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
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "MSI GF63 Thin 11SC GTX1650 Max Q Gaming Laptop 15.6' FHD"},
                {SpecsType.operatingSystem.name(), "Windows 11 Home"},
                {SpecsType.display.name(), "15.6\" FHD (1920*1080) 60Hz"},
                {SpecsType.cpu.name(), "11th Gen. Intel® Core™ i5-11400H Processor 6 Cores"},
                {SpecsType.gpu.name(), "NVIDIA® GeForce® GTX 1650 Laptop GPU, 4GB GDDR6"},
                {SpecsType.camera.name(), "HD type (30fps @ 720p)"},
                {SpecsType.keyboard.name(), "Backlight Keyboard (Single-Color, Red)"},
                {SpecsType.communication.name(), "802.11 ax Wi-Fi 6 + Bluetooth v5.2"},
                {SpecsType.audio.name(), "2x 2W Speaker; 1x Mic-in; 1x Headphone-out"},
                {SpecsType.touchscreen.name(), "N/A"},
                {SpecsType.fingerprintReader.name(), "N/A"},
                {SpecsType.opticalDrive.name(), "N/A"},
                {SpecsType.ports.name(), "1x RJ45; 1x (4K @ 30Hz) HDMI; 1x Type-C USB3.2 Gen1; 3x Type-A USB3.2 Gen1"},
                {SpecsType.battery.name(), "3-Cell, 51 Battery (Whr)"},
                {SpecsType.acAdaptor.name(), "120W adapter"},
                {SpecsType.dimensions.name(), "359 x 254 x 21.7 mm"},
                {SpecsType.weight.name(), "1.86 kg"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.ram.name(), rams);
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_msi_gf63_1_black",
                "item_laptop_msi_gf63_2_black",
                "item_laptop_msi_gf63_3_black"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                "Logitech Pro X DTS Headphone",
                "HyperX QuadCast S Standalone Microphone",
                "Sony WH-1000XM4"
        ));
        items.add(new Laptop("MSI GF63",
                "MSI",
                specs,
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
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 3099.99)
        ));
        rams = new ArrayList<>(Arrays.asList(
                new SpecsOption(16, 0),
                new SpecsOption(32, 300)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(1024, 0)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Razer Blade 17 - Full HD 360Hz - GeForce RTX 3070 Ti - Black"},
                {SpecsType.operatingSystem.name(), "Windows 11 Home"},
                {SpecsType.display.name(), "17.3\" FHD 360Hz; 100% sRGB; 6mm bezels; individually factory calibrated; 3ms Response Rate"},
                {SpecsType.cpu.name(), "1.8GHz 14-core Intel i7-12800H processor; Turbo Boost up to 4.8GHz, with 24MB of Cache"},
                {SpecsType.gpu.name(), "NVIDIA® GeForce RTX™ 3070 Ti (8GB GDDR6 VRAM)"},
                {SpecsType.camera.name(), "Built-in Full HD webcam"},
                {SpecsType.keyboard.name(), "Per-Key Backlighting, powered by Razer Chroma"},
                {SpecsType.communication.name(), "Wireless Wi-Fi 6E AX1690 (IEEE 802.11a/b/g/n/ac/ax/az); Bluetooth® 5.2"},
                {SpecsType.audio.name(), "3.5mm Combo-Jack; Stereo 2.0 | 8 Speakers; THX Spatial Audio; 2-Mic Array"},
                {SpecsType.touchscreen.name(), "N/A"},
                {SpecsType.fingerprintReader.name(), "N/A"},
                {SpecsType.opticalDrive.name(), "N/A"},
                {SpecsType.ports.name(), "2 x Thunderbolt™ 4 (USB-C™); 1 x USB-C 3.2 Gen 2 - Supports Power Delivery 3 (15W); 3 x USB-A 3.2 Gen 2"},
                {SpecsType.battery.name(), "Built-in 82WHr rechargeable lithium-ion polymer battery"},
                {SpecsType.acAdaptor.name(), "280W power adapter"},
                {SpecsType.dimensions.name(), "395 x 260 x 19.9 mm"},
                {SpecsType.weight.name(), "2.75 kg"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.ram.name(), rams);
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_razer_blade_17_1_black",
                "item_laptop_razer_blade_17_2_black",
                "item_laptop_razer_blade_17_3_black"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                "Logitech Pro X DTS Headphone",
                "HyperX QuadCast S Standalone Microphone",
                "Sony WH-1000XM4"
        ));
        items.add(new Laptop("Razer Blade 17 - Full HD 360Hz - GeForce RTX 3070 Ti - Black",
                "Razer",
                specs,
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
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Acer Aspire 7 A715-42G-R0EX"},
                {SpecsType.operatingSystem.name(), "Windows 10 Home"},
                {SpecsType.display.name(), "39.6 cm (15.6\") LCD"},
                {SpecsType.cpu.name(), "AMD Ryzen 5 5500U Hexa-core (6 Core™) 2.10 GHz; up to 4 GHz; 8 MB Cache"},
                {SpecsType.gpu.name(), "NVIDIA® GeForce® GTX 1650 4 GB GDDR6"},
                {SpecsType.camera.name(), "Front Camera/Webcam, 1280 x 720"},
                {SpecsType.keyboard.name(), "Backlight"},
                {SpecsType.communication.name(), "802.11 ax Wi-Fi 6 + Bluetooth"},
                {SpecsType.audio.name(), "Microphone; Stereo speakers"},
                {SpecsType.touchscreen.name(), "N/A"},
                {SpecsType.fingerprintReader.name(), "Yes"},
                {SpecsType.opticalDrive.name(), "N/A"},
                {SpecsType.ports.name(), "1x HDMI; 1x USB 2.0 Ports; 2x USB 3.2 Gen 1 Type-A Ports; 1x USB 3.2 Gen 1 Type-C Ports (up to 5 Gbps); 1x Network (RJ-45); 1x Headphone/Microphone Combo Port"},
                {SpecsType.battery.name(), "3-cell Lithium Ion (Li-Ion) 48 Wh"},
                {SpecsType.acAdaptor.name(), "135W adapter"},
                {SpecsType.dimensions.name(), "363.4 x 254.5 x 22.9 mm"},
                {SpecsType.weight.name(), "2.15 kg"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.ram.name(), rams);
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_acer_aspire_7_a715_42g_roex_1_black",
                "item_laptop_acer_aspire_7_a715_42g_roex_2_black",
                "item_laptop_acer_aspire_7_a715_42g_roex_3_black"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                "Logitech Pro X DTS Headphone",
                "HyperX QuadCast S Standalone Microphone",
                "Sony WH-1000XM4"
        ));
        items.add(new Laptop("Acer Aspire 7 A715-42G-R0EX",
                "Acer",
                specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Laptop 10.5.1
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 829),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 809),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 869),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 899)
        ));
        rams = new ArrayList<>(Arrays.asList(
                new SpecsOption(8, 0),
                new SpecsOption(16, 69)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(1024, 0)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Acer Aspire 3 14in FHD Athlon Silver 3050U 8GB RAM 128GB SSD+1TB HDD"},
                {SpecsType.operatingSystem.name(), "Windows 10 Home"},
                {SpecsType.display.name(), "39.6 cm (15.6\") LCD"},
                {SpecsType.cpu.name(), "AMD Ryzen 5 5500U Hexa-core (6 Core™) 2.10 GHz; up to 4 GHz; 8 MB Cache"},
                {SpecsType.gpu.name(), "NVIDIA® GeForce® GTX 1650 4 GB GDDR6"},
                {SpecsType.camera.name(), "Front Camera/Webcam, 1280 x 720"},
                {SpecsType.keyboard.name(), "Backlight"},
                {SpecsType.communication.name(), "802.11 ax Wi-Fi 6 + Bluetooth"},
                {SpecsType.audio.name(), "Microphone; Stereo speakers"},
                {SpecsType.touchscreen.name(), "N/A"},
                {SpecsType.fingerprintReader.name(), "Yes"},
                {SpecsType.opticalDrive.name(), "N/A"},
                {SpecsType.ports.name(), "1x HDMI; 1x USB 2.0 Ports; 2x USB 3.2 Gen 1 Type-A Ports; 1x USB 3.2 Gen 1 Type-C Ports (up to 5 Gbps); 1x Network (RJ-45); 1x Headphone/Microphone Combo Port"},
                {SpecsType.battery.name(), "3-cell Lithium Ion (Li-Ion) 48 Wh"},
                {SpecsType.acAdaptor.name(), "135W adapter"},
                {SpecsType.dimensions.name(), "363.4 x 254.5 x 22.9 mm"},
                {SpecsType.weight.name(), "2.15 kg"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.ram.name(), rams);
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_acer_aspire_3_1_black",
                "item_laptop_acer_aspire_3_2_black",
                "item_laptop_acer_aspire_3_3_black"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                "Logitech Pro X DTS Headphone",
                "HyperX QuadCast S Standalone Microphone",
                "Sony WH-1000XM4"
        ));
        items.add(new Laptop("Acer Aspire 3",
                "Acer",
                specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Laptop 10.5.2
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("silver", "#C0C0C0")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 729),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 709),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 769),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 799)
        ));
        rams = new ArrayList<>(Arrays.asList(
                new SpecsOption(8, 0),
                new SpecsOption(16, 69)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(1024, 0)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Acer Spin 1 SP114-31-P3KD Edu Laptop 14\" FHD Touch Intel Pentium Silver N6000"},
                {SpecsType.operatingSystem.name(), "Windows 10 Home"},
                {SpecsType.display.name(), "39.6 cm (15.6\") LCD"},
                {SpecsType.cpu.name(), "AMD Ryzen 5 5500U Hexa-core (6 Core™) 2.10 GHz; up to 4 GHz; 8 MB Cache"},
                {SpecsType.gpu.name(), "NVIDIA® GeForce® GTX 1650 4 GB GDDR6"},
                {SpecsType.camera.name(), "Front Camera/Webcam, 1280 x 720"},
                {SpecsType.keyboard.name(), "Backlight"},
                {SpecsType.communication.name(), "802.11 ax Wi-Fi 6 + Bluetooth"},
                {SpecsType.audio.name(), "Microphone; Stereo speakers"},
                {SpecsType.touchscreen.name(), "N/A"},
                {SpecsType.fingerprintReader.name(), "Yes"},
                {SpecsType.opticalDrive.name(), "N/A"},
                {SpecsType.ports.name(), "1x HDMI; 1x USB 2.0 Ports; 2x USB 3.2 Gen 1 Type-A Ports; 1x USB 3.2 Gen 1 Type-C Ports (up to 5 Gbps); 1x Network (RJ-45); 1x Headphone/Microphone Combo Port"},
                {SpecsType.battery.name(), "3-cell Lithium Ion (Li-Ion) 48 Wh"},
                {SpecsType.acAdaptor.name(), "135W adapter"},
                {SpecsType.dimensions.name(), "363.4 x 254.5 x 22.9 mm"},
                {SpecsType.weight.name(), "2.15 kg"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.ram.name(), rams);
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_laptop_acer_spin_1_1_black",
                "item_laptop_acer_spin_1_2_black",
                "item_laptop_acer_spin_1_3_black"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                "Logitech Pro X DTS Headphone",
                "HyperX QuadCast S Standalone Microphone",
                "Sony WH-1000XM4"
        ));
        items.add(new Laptop("Acer Spin 1",
                "Acer",
                specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        return items;
    }

    /**
     * ITEMS.PHONES
     */
    private static List<IItem> generatePhones() {
        List<IItem> items = new ArrayList<>();
        List<String> imageUris, recommendedAccessoryIds;
        Map<String, String> fixedSpecs;
        Map<String, List<SpecsOption>> customisableSpecs;
        Specs specs;
        List<SpecsOption> storages;
        List<StoreVariant> itemStoreVariant;
        List<Colour> colours;

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
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Samsung Galaxy S22 Ultra"},
                {SpecsType.operatingSystem.name(), "android"},
                {SpecsType.display.name(), "6.8\" edge Quad HD+ Dynamic AMOLED 2X; Infinity-O Display (3088x1440); 120Hz refresh rate"},
                {SpecsType.cpu.name(), "Snapdragon 8 Gen 1 4nm octa-core flagship processor"},
                {SpecsType.camera.name(), "Quad Rear Camera; 108MP Main sensor, F1.8, OIS; 12MP Ultra Wide angle, F2.2; 10MP 3x Telephoto, F2.4, OIS; 10MP 10x Periscope Telephoto, F4.9, OIS; Up to 100x Space Zoom; 40MP Front camera, F2.2"},
                {SpecsType.communication.name(), "5G; LTE; Wi-Fi 802.11; Bluetooth v5.2"},
                {SpecsType.audio.name(), "Stereo speakers; Ultra high quality audio playback; Audio playback format"},
                {SpecsType.touchscreen.name(), "Yes"},
                {SpecsType.protectionResistance.name(), "IP68"},
                {SpecsType.simCard.name(), "Dual SIM model"},
                {SpecsType.sensors.name(), "Ultrasonic Fingerprint sensor; Geomagnetic sensor; Accelerometer; Hall sensor; Barometer; Proximity sensor; Gyro sensor; Ambient Light sensor"},
                {SpecsType.battery.name(), "5000mAh"},
                {SpecsType.dimensions.name(), "77.9 x 163.3 x 8.9mm"},
                {SpecsType.weight.name(), "228g"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
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
                "Urban Armor Gear - Galaxy S22 Ultra 5G",
                "Xiaomi Mi 20000mAh 50W Fast Charging Power Bank",
                "Sony WH-1000XM4"
        ));
        items.add(new Phone("Samsung Galaxy S22 Ultra",
                "Samsung",
                specs,
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
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Samsung Galaxy S22"},
                {SpecsType.operatingSystem.name(), "android"},
                {SpecsType.display.name(), "6.1\" Dynamic AMOLED 2X display; FHD+ (2340x1080); 120Hz refresh rate"},
                {SpecsType.cpu.name(), "Snapdragon 8 Gen 1 4nm octa-core flagship processor"},
                {SpecsType.camera.name(), "Triple Rear Camera; 50MP main sensor, F1.8, OIS; 12MP Ultra-Wide angle, F2.2; 10MP 3x Telephoto, F2.4, OIS; 30x Digital Zoom; 10MP Front camera, F2.2, Autofocus"},
                {SpecsType.communication.name(), "5G; LTE; Wi-Fi 802.11; Bluetooth v5.2"},
                {SpecsType.audio.name(), "Duel stereo speakers"},
                {SpecsType.touchscreen.name(), "Yes"},
                {SpecsType.protectionResistance.name(), "IP68"},
                {SpecsType.simCard.name(), "Dual SIM model"},
                {SpecsType.sensors.name(), "Ultrasonic Fingerprint sensor; Geomagnetic sensor; Accelerometer; Hall sensor; Barometer; Proximity sensor; Gyro sensor; Ambient Light sensor"},
                {SpecsType.battery.name(), "3700mAh"},
                {SpecsType.dimensions.name(), "70.6 x 146 x 7.6mm"},
                {SpecsType.weight.name(), "167g"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
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
                "BOOM 3 Bluetooth Speaker",
                "Xiaomi Mi 20000mAh 50W Fast Charging Power Bank",
                "Sony WH-1000XM4"
        ));
        items.add(new Phone("Samsung Galaxy S22",
                "Samsung",
                specs,
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
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Apple iPhone 13 Pro Max"},
                {SpecsType.operatingSystem.name(), "ios"},
                {SpecsType.display.name(), "6.7‑inch (diagonal) all‑screen OLED display; Super Retina XDR display; (2778x1284) pixel resolution at 458 ppi"},
                {SpecsType.cpu.name(), "A15 Bionic chip"},
                {SpecsType.camera.name(), "Telephoto, Wide and Ultra Wide cameras; 3x optical zoom in, 2x optical zoom out; 6x optical zoom range; Digital zoom up to 15x; Night mode portraits enabled by LiDAR Scanner"},
                {SpecsType.communication.name(), "5G; LTE; Wi-Fi 802.11ax; Bluetooth 5.0"},
                {SpecsType.audio.name(), "Stereo speakers; Spatial audio playback"},
                {SpecsType.touchscreen.name(), "Yes"},
                {SpecsType.protectionResistance.name(), "IP68"},
                {SpecsType.simCard.name(), "Dual SIM (nano‑SIM and eSIM); Dual eSIM support"},
                {SpecsType.sensors.name(), "Face ID; LiDAR Scanner; Barometer; Three‑axis gyro; Accelerometer; Proximity sensor; Ambient light sensor"},
                {SpecsType.battery.name(), "Built‑in rechargeable lithium‑ion battery"},
                {SpecsType.dimensions.name(), "78.1 x 160.8 x 7.65mm"},
                {SpecsType.weight.name(), "238g"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
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
                "Apple iPhone 13 Pro Max Leather Case with MagSafe",
                "Apple AirPods Pro",
                "Xiaomi Mi 20000mAh 50W Fast Charging Power Bank"
        ));
        items.add(new Phone("Apple iPhone 13 Pro Max",
                "Apple",
                specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Phone 3.5
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("green", "#576856"),
                new Colour("silver", "#F2F3EE"),
                new Colour("gold", "#FAEAD3"),
                new Colour("graphite", "#5F5E5A"),
                new Colour("blue", "#AFC6DC")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 1699),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 1658.99),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 1699),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 1658.99)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(128, 0),
                new SpecsOption(256, 200),
                new SpecsOption(512, 600),
                new SpecsOption(1024, 1000)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Apple iPhone 13 Pro"},
                {SpecsType.operatingSystem.name(), "ios"},
                {SpecsType.display.name(), "6.7‑inch (diagonal) all‑screen OLED display; Super Retina XDR display; (2778x1284) pixel resolution at 458 ppi"},
                {SpecsType.cpu.name(), "A15 Bionic chip"},
                {SpecsType.camera.name(), "Telephoto, Wide and Ultra Wide cameras; 3x optical zoom in, 2x optical zoom out; 6x optical zoom range; Digital zoom up to 15x; Night mode portraits enabled by LiDAR Scanner"},
                {SpecsType.communication.name(), "5G; LTE; Wi-Fi 802.11ax; Bluetooth 5.0"},
                {SpecsType.audio.name(), "Stereo speakers; Spatial audio playback"},
                {SpecsType.touchscreen.name(), "Yes"},
                {SpecsType.protectionResistance.name(), "IP68"},
                {SpecsType.simCard.name(), "Dual SIM (nano‑SIM and eSIM); Dual eSIM support"},
                {SpecsType.sensors.name(), "Face ID; LiDAR Scanner; Barometer; Three‑axis gyro; Accelerometer; Proximity sensor; Ambient light sensor"},
                {SpecsType.battery.name(), "Built‑in rechargeable lithium‑ion battery"},
                {SpecsType.dimensions.name(), "78.1 x 160.8 x 7.65mm"},
                {SpecsType.weight.name(), "238g"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_phone_iphone_13_pro_max_2_green",
                "item_phone_iphone_13_pro_max_1_green",
                "item_phone_iphone_13_pro_max_3_green",

                "item_phone_iphone_13_pro_max_2_silver",
                "item_phone_iphone_13_pro_max_1_silver",
                "item_phone_iphone_13_pro_max_3_silver",

                "item_phone_iphone_13_pro_max_2_gold",
                "item_phone_iphone_13_pro_max_1_gold",
                "item_phone_iphone_13_pro_max_3_gold",

                "item_phone_iphone_13_pro_max_2_graphite",
                "item_phone_iphone_13_pro_max_1_graphite",
                "item_phone_iphone_13_pro_max_3_graphite",

                "item_phone_iphone_13_pro_max_2_blue",
                "item_phone_iphone_13_pro_max_1_blue",
                "item_phone_iphone_13_pro_max_3_blue"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                "Sony WH-1000XM4",
                "Apple AirPods Pro",
                "Xiaomi Mi 20000mAh 50W Fast Charging Power Bank"
        ));
        items.add(new Phone("Apple iPhone 13 Pro",
                "Apple",
                specs,
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
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Apple iPhone 13"},
                {SpecsType.operatingSystem.name(), "ios"},
                {SpecsType.display.name(), "6.1‑inch (diagonal) all‑screen OLED display; Super Retina XDR display; (2532x1170) pixel resolution at 460 ppi"},
                {SpecsType.cpu.name(), "A15 Bionic chip"},
                {SpecsType.camera.name(), "Wide and Ultra Wide cameras; 2x optical zoom out; Digital zoom up to 5x"},
                {SpecsType.communication.name(), "5G; LTE; Wi-Fi 802.11ax; Bluetooth 5.0"},
                {SpecsType.audio.name(), "Stereo speakers; Spatial audio playback"},
                {SpecsType.touchscreen.name(), "Yes"},
                {SpecsType.protectionResistance.name(), "IP68"},
                {SpecsType.simCard.name(), "Dual SIM (nano‑SIM and eSIM); Dual eSIM support"},
                {SpecsType.sensors.name(), "Face ID; Barometer; Three‑axis gyro; Accelerometer; Proximity sensor; Ambient light sensor"},
                {SpecsType.battery.name(), "Built‑in rechargeable lithium‑ion battery"},
                {SpecsType.dimensions.name(), "71.5 x 146.7 x 7.65mm"},
                {SpecsType.weight.name(), "173g"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
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
                "Sony WH-1000XM4",
                "Apple AirPods Pro",
                "Xiaomi Mi 20000mAh 50W Fast Charging Power Bank"
        ));
        items.add(new Phone("Apple iPhone 13",
                "Apple",
                specs,
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
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Apple iPhone SE"},
                {SpecsType.operatingSystem.name(), "ios"},
                {SpecsType.display.name(), "4.7-inch (diagonal) widescreen LCD; Retina HD display; (1334x750) pixel resolution at 326 ppi"},
                {SpecsType.cpu.name(), "A15 Bionic chip"},
                {SpecsType.camera.name(), "12MP Wide camera; Digital zoom up to 5x"},
                {SpecsType.communication.name(), "5G; LTE; Wi-Fi 802.11ax; Bluetooth 5.0"},
                {SpecsType.audio.name(), "Stereo speakers; Spatial audio playback"},
                {SpecsType.touchscreen.name(), "Yes"},
                {SpecsType.protectionResistance.name(), "IP67"},
                {SpecsType.simCard.name(), "Dual SIM (nano‑SIM and eSIM); Dual eSIM support"},
                {SpecsType.sensors.name(), "Touch ID fingerprint sensor; Barometer; Three‑axis gyro; Accelerometer; Proximity sensor; Ambient light sensor"},
                {SpecsType.battery.name(), "Built‑in rechargeable lithium‑ion battery"},
                {SpecsType.dimensions.name(), "67.3 x 138.4 x 7.3mm"},
                {SpecsType.weight.name(), "144g"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
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
                "Lifeproof iPhone SE case",
                "Apple AirPods Pro",
                "Xiaomi Mi 20000mAh 50W Fast Charging Power Bank"
        ));
        items.add(new Phone("Apple iPhone SE",
                "Apple",
                specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Phone 5.5.1
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("green", "#ADE0CD"),
                new Colour("purple", "#D1CDDB"),
                new Colour("white", "#F9F6EF"),
                new Colour("yellow", "#FFE680"),
                new Colour("black", "#1F2120"),
                new Colour("red", "#C92435")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 429),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 428.99),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 429),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 428.99)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(64, 0),
                new SpecsOption(128, 100)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Apple iPhone 11"},
                {SpecsType.operatingSystem.name(), "ios"},
                {SpecsType.display.name(), "6.1‑inch (diagonal) all‑screen OLED display; Super Retina XDR display; (2532x1170) pixel resolution at 460 ppi"},
                {SpecsType.cpu.name(), "A15 Bionic chip"},
                {SpecsType.camera.name(), "Wide and Ultra Wide cameras; 2x optical zoom out; Digital zoom up to 5x"},
                {SpecsType.communication.name(), "5G; LTE; Wi-Fi 802.11ax; Bluetooth 5.0"},
                {SpecsType.audio.name(), "Stereo speakers; Spatial audio playback"},
                {SpecsType.touchscreen.name(), "Yes"},
                {SpecsType.protectionResistance.name(), "IP68"},
                {SpecsType.simCard.name(), "Dual SIM (nano‑SIM and eSIM); Dual eSIM support"},
                {SpecsType.sensors.name(), "Face ID; Barometer; Three‑axis gyro; Accelerometer; Proximity sensor; Ambient light sensor"},
                {SpecsType.battery.name(), "Built‑in rechargeable lithium‑ion battery"},
                {SpecsType.dimensions.name(), "71.5 x 146.7 x 7.65mm"},
                {SpecsType.weight.name(), "173g"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_phone_iphone_11_1_green",
                "item_phone_iphone_11_2_green",
                "item_phone_iphone_11_3_green",

                "item_phone_iphone_11_1_purple",
                "item_phone_iphone_11_2_purple",
                "item_phone_iphone_11_3_purple",

                "item_phone_iphone_11_1_white",
                "item_phone_iphone_11_2_white",
                "item_phone_iphone_11_3_white",

                "item_phone_iphone_11_1_yellow",
                "item_phone_iphone_11_2_yellow",
                "item_phone_iphone_11_3_yellow",

                "item_phone_iphone_11_1_black",
                "item_phone_iphone_11_2_black",
                "item_phone_iphone_11_3_black",

                "item_phone_iphone_11_1_red",
                "item_phone_iphone_11_2_red",
                "item_phone_iphone_11_3_red"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                "Sony WH-1000XM4",
                "Apple AirPods Pro",
                "Xiaomi Mi 20000mAh 50W Fast Charging Power Bank"
        ));
        items.add(new Phone("Apple iPhone 11",
                "Apple",
                specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Phone 5.5.2
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#FFFFFF"),
                new Colour("white", "#000000")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 199),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 299),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 199),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 199)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(8, 0),
                new SpecsOption(16, 100),
                new SpecsOption(32, 300)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Apple iPhone 4"},
                {SpecsType.operatingSystem.name(), "ios"},
                {SpecsType.display.name(), "4.7-inch (diagonal) widescreen LCD; Retina HD display; (1334x750) pixel resolution at 326 ppi"},
                {SpecsType.cpu.name(), "A15 Bionic chip"},
                {SpecsType.camera.name(), "12MP Wide camera; Digital zoom up to 5x"},
                {SpecsType.communication.name(), "5G; LTE; Wi-Fi 802.11ax; Bluetooth 5.0"},
                {SpecsType.audio.name(), "Stereo speakers; Spatial audio playback"},
                {SpecsType.touchscreen.name(), "Yes"},
                {SpecsType.protectionResistance.name(), "IP67"},
                {SpecsType.simCard.name(), "Dual SIM (nano‑SIM and eSIM); Dual eSIM support"},
                {SpecsType.sensors.name(), "Touch ID fingerprint sensor; Barometer; Three‑axis gyro; Accelerometer; Proximity sensor; Ambient light sensor"},
                {SpecsType.battery.name(), "Built‑in rechargeable lithium‑ion battery"},
                {SpecsType.dimensions.name(), "67.3 x 138.4 x 7.3mm"},
                {SpecsType.weight.name(), "144g"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_phone_iphone_4_1_black",
                "item_phone_iphone_4_2_black",
                "item_phone_iphone_4_3_black",

                "item_phone_iphone_4_1_white",
                "item_phone_iphone_4_2_white",
                "item_phone_iphone_4_3_white"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                "Sony WH-1000XM4",
                "Apple AirPods Pro",
                "Xiaomi Mi 20000mAh 50W Fast Charging Power Bank"
        ));
        items.add(new Phone("Apple iPhone 4",
                "Apple",
                specs,
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
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "OnePlus 10 Pro"},
                {SpecsType.operatingSystem.name(), "android"},
                {SpecsType.display.name(), "17.02 centimeters (6.7 inches) (measured diagonally from corner to corner); 3216 X 1440 pixels 525 ppi; 120 Hz Fluid AMOLED with LTPO"},
                {SpecsType.cpu.name(), "Snapdragon® 8 Gen 1 Mobile Platform"},
                {SpecsType.camera.name(), "Main Camera; Ultra-Wide Camera; Telephoto Camera"},
                {SpecsType.communication.name(), "5G; LTE; Wi-Fi 802.11a/b/g/n/ac/ax; Bluetooth v5.2"},
                {SpecsType.audio.name(), "Dual Stereo Speakers; Noise cancellation support; Dolby Atmos"},
                {SpecsType.touchscreen.name(), "Yes"},
                {SpecsType.protectionResistance.name(), "IP68"},
                {SpecsType.simCard.name(), "Dual nano-SIM slot"},
                {SpecsType.sensors.name(), "In-display Fingerprint Sensor; Accelerometer; Electronic Compass; Gyroscope; Ambient Light Sensor; Proximity Sensor; Sensor Core; Flicker-detect Sensor; Front RGB sensor"},
                {SpecsType.battery.name(), "5000mAh (2S1P 2,500 mAh, non-removable)"},
                {SpecsType.dimensions.name(), "73.9 x 163.0 x 8.55mm"},
                {SpecsType.weight.name(), "200.5g"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_phone_oneplus_10_pro_1_emerald",
                "item_phone_oneplus_10_pro_2_emerald",
                "item_phone_oneplus_10_pro_3_emerald",

                "item_phone_oneplus_10_pro_1_black",
                "item_phone_oneplus_10_pro_2_black",
                "item_phone_oneplus_10_pro_3_black"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                "Spigen Oneplus 10 Pro Hybrid Case",
                "Sony WH-1000XM4",
                "Xiaomi Mi 20000mAh 50W Fast Charging Power Bank"
        ));
        items.add(new Phone("OnePlus 10 Pro",
                "OnePlus",
                specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Phone 7
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("silver", "#82878B"),
                new Colour("blue", "#00A0BA")
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
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "HUAWEI P40 Pro"},
                {SpecsType.operatingSystem.name(), "android"},
                {SpecsType.display.name(), "6.58 inches; OLED, up to 90 Hz frame refresh rate; 2640 x 1200 Pixels"},
                {SpecsType.cpu.name(), "HUAWEI Kirin 990 5G; Octa-core"},
                {SpecsType.camera.name(), "Rear Camera; 50 MP Ultra Vision Camera + 40 MP Cine Camera + 3D Depth Sensing Camera"},
                {SpecsType.communication.name(), "5G; LTE; Wi-Fi 802.11; Bluetooth v5.2"},
                {SpecsType.audio.name(), "Stereo speakers; Audio playback format"},
                {SpecsType.touchscreen.name(), "Yes"},
                {SpecsType.protectionResistance.name(), "IP68"},
                {SpecsType.simCard.name(), "Dual SIM model"},
                {SpecsType.sensors.name(), "Gesture Sensor; Gravity Sensor; Infrared Sensor; Fingerprint Sensor; Hall Sensor; Gyroscope; Compass; Ambient Light Sensor; Proximity Sensor; Colour Temperature Sensor"},
                {SpecsType.battery.name(), "4200 mAh"},
                {SpecsType.dimensions.name(), "72.6 x 158.2 x 8.95mm"},
                {SpecsType.weight.name(), "209g"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_phone_huawei_p40_pro_1_silver",
                "item_phone_huawei_p40_pro_2_silver",
                "item_phone_huawei_p40_pro_3_silver",

                "item_phone_huawei_p40_pro_1_blue",
                "item_phone_huawei_p40_pro_2_blue",
                "item_phone_huawei_p40_pro_3_blue"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                "BOOM 3 Bluetooth Speaker",
                "Sony WH-1000XM4",
                "Xiaomi Mi 20000mAh 50W Fast Charging Power Bank"
        ));
        items.add(new Phone("HUAWEI P40 Pro",
                "HUAWEI",
                specs,
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
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Xiaomi 12 Pro"},
                {SpecsType.operatingSystem.name(), "android"},
                {SpecsType.display.name(), "WQHD+ 6.73\" AMOLED DotDisplay; 3200 x 1440; 552ppi; 480Hz"},
                {SpecsType.cpu.name(), "Snapdragon® 8 Gen 1"},
                {SpecsType.camera.name(), "Pro-grade 50MP triple camera array; 50MP wide angle camera + 50MP ultra-wide angle camera + 50MP telephoto camera; 32MP in-display selfie camera"},
                {SpecsType.communication.name(), "5G; LTE; Wi-Fi 6 / Wi-Fi 6E; Bluetooth v5.2"},
                {SpecsType.audio.name(), "Quad speakers; Dolby Atmos®; SOUND BY Harman Kardon"},
                {SpecsType.touchscreen.name(), "Yes"},
                {SpecsType.protectionResistance.name(), "IP68"},
                {SpecsType.simCard.name(), "Dual SIM model"},
                {SpecsType.sensors.name(), "In-screen fingerprint sensor; AI face unlock; Proximity sensor | Ambient light sensor | Accelerometer | Gyroscope | Electronic compass | Linear motor | IR blaster | Barometer | Flicker sensor"},
                {SpecsType.battery.name(), "4600mAh"},
                {SpecsType.dimensions.name(), "74.6 x 163.6 x 8.16mm"},
                {SpecsType.weight.name(), "205g"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
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
                "Xiaomi Mi 20000mAh 50W Fast Charging Power Bank",
                "BOOM 3 Bluetooth Speaker",
                "Sony WH-1000XM4"
        ));
        items.add(new Phone("Xiaomi 12 Pro",
                "Xiaomi",
                specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Phone 9
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("blue", "#4E8EA0"),
                new Colour("dusk", "#A09591")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 299),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 299)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(128, 0)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Nokia G21"},
                {SpecsType.operatingSystem.name(), "android"},
                {SpecsType.display.name(), "6.5 inch (1600*720); Features:Adaptive 90Hz refresh rate and 180Hz touch sampling rate"},
                {SpecsType.cpu.name(), "Unisoc T606"},
                {SpecsType.camera.name(), "Front camera: 8 MP; Rear camera: 50 MP Main 1/2.76“ CMOS, 0.64um, 5P lens, f/1.8 + 2 MP Macro + 2 MP Depth; Rear flash LED"},
                {SpecsType.communication.name(), "4G; Wi-Fi 802.11 a/b/g/n/ac; Bluetooth 5.0"},
                {SpecsType.audio.name(), "OZO Spatial Audio capture. FM Radio (Headset required); Microphone"},
                {SpecsType.touchscreen.name(), "Yes"},
                {SpecsType.protectionResistance.name(), "N/A"},
                {SpecsType.simCard.name(), "Nano SIM"},
                {SpecsType.sensors.name(), "Fingerprint sensor: Side Power key; Face Unlock; Accelerometer (G-sensor); Ambient light sensor; Proximity sensor"},
                {SpecsType.battery.name(), "5050mAh"},
                {SpecsType.dimensions.name(), "75.9 x 164.6 x 8.5mm"},
                {SpecsType.weight.name(), "190g"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_phone_nokia_g21_1_blue",
                "item_phone_nokia_g21_2_blue",
                "item_phone_nokia_g21_3_blue",

                "item_phone_nokia_g21_1_dusk",
                "item_phone_nokia_g21_2_dusk",
                "item_phone_nokia_g21_3_dusk"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                "BOOM 3 Bluetooth Speaker",
                "Sony WH-1000XM4",
                "Xiaomi Mi 20000mAh 50W Fast Charging Power Bank"
        ));
        items.add(new Phone("Nokia G21",
                "Nokia",
                specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        /*
         * Phone 10
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#242424"),
                new Colour("sand", "#B3856D"),
                new Colour("blue", "#294970")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 96.58),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 94.96)
        ));
        storages = new ArrayList<>(Arrays.asList(
                new SpecsOption(2, 0)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Nokia 225 4G"},
                {SpecsType.operatingSystem.name(), "android"},
                {SpecsType.display.name(), "2.4 inch; QVGA resolution"},
                {SpecsType.cpu.name(), "Unisoc T117"},
                {SpecsType.camera.name(), "Rear camera: 0.3 MP"},
                {SpecsType.communication.name(), "4G; Bluetooth 5.0"},
                {SpecsType.audio.name(), "FM Radio (Wired & Wireless dual mode); MP3 player"},
                {SpecsType.touchscreen.name(), "No"},
                {SpecsType.protectionResistance.name(), "It's a brick"},
                {SpecsType.simCard.name(), "Nano SIM"},
                {SpecsType.sensors.name(), "N/A"},
                {SpecsType.battery.name(), "1150mAh"},
                {SpecsType.dimensions.name(), "51 x 124.7 x 13.7mm"},
                {SpecsType.weight.name(), "90.1g"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        customisableSpecs = new HashMap<>();
        customisableSpecs.put(SpecsOptionType.storage.name(), storages);
        specs = new Specs(fixedSpecs, customisableSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_phone_nokia_225_4g_1_black",
                "item_phone_nokia_225_4g_2_black",
                "item_phone_nokia_225_4g_3_black",

                "item_phone_nokia_225_4g_1_sand",
                "item_phone_nokia_225_4g_2_sand",
                "item_phone_nokia_225_4g_3_sand",

                "item_phone_nokia_225_4g_1_blue",
                "item_phone_nokia_225_4g_2_blue",
                "item_phone_nokia_225_4g_3_blue"
        ));
        recommendedAccessoryIds = new ArrayList<>(Arrays.asList(
                "BOOM 3 Bluetooth Speaker",
                "Sony WH-1000XM4",
                "Xiaomi Mi 20000mAh 50W Fast Charging Power Bank"
        ));
        items.add(new Phone("Nokia 225 4G",
                "Nokia",
                specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        return items;
    }

    /**
     * ITEMS.ACCESSORIES
     */
    private static List<IItem> generateAccessories() {
        List<IItem> items = new ArrayList<>();
        List<String> imageUris;
        Map<String, String> fixedSpecs;
        Specs specs;
        List<StoreVariant> itemStoreVariant;
        List<Colour> colours;

        /*
         * Accessory 1 - all - Sony WH-1000XM4
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
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Sony WH-1000XM4 Wireless Over-Ear Noise-Cancelling Headphones"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        specs = new Specs(fixedSpecs);
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
                specs,
                itemStoreVariant,
                imageUris,
                true,
                true));

        /*
         * Accessory 2 - gaming laptop - Logitech Pro X DTS Headphone
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 299),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 299),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 299)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Logitech Pro X DTS Headphone: X 2.0 Gaming Headset With Blue Vo!ce"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        specs = new Specs(fixedSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_logitech_pro_x_dts_1_black",
                "item_accessory_logitech_pro_x_dts_2_black",
                "item_accessory_logitech_pro_x_dts_3_black"
        ));
        items.add(new Accessory("Logitech Pro X DTS Headphone",
                "Logitech",
                specs,
                itemStoreVariant,
                imageUris,
                true,
                false));

        /*
         * Accessory 3 - all - Apple AirPods Pro
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("white", "#FFFFFF")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 459),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 438.99),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 449),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 454.99)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Apple AirPods Pro Noise Cancelling True Wireless Headphones - with MagSafe charging case"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        specs = new Specs(fixedSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_apple_airpods_pro_1_white",
                "item_accessory_apple_airpods_pro_2_white",
                "item_accessory_apple_airpods_pro_3_white"
        ));
        items.add(new Accessory("Apple AirPods Pro",
                "Apple",
                specs,
                itemStoreVariant,
                imageUris,
                true,
                true));

        /*
         * Accessory 4 - all - BOOM 3 Bluetooth Speaker
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000"),
                new Colour("blue", "#3C5780"),
                new Colour("purple", "#705A8F"),
                new Colour("red", "#A83D4A"),
                new Colour("peach", "#D0D0EB")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 258.99),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 259.99),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 259.99),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 258.99)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Ultimate Ears UE BOOM 3 Wireless Portable Bluetooth Speaker"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        specs = new Specs(fixedSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_boom3_1_black",
                "item_accessory_boom3_2_black",
                "item_accessory_boom3_3_black",

                "item_accessory_boom3_1_blue",
                "item_accessory_boom3_2_blue",
                "item_accessory_boom3_3_blue",

                "item_accessory_boom3_1_purple",
                "item_accessory_boom3_2_purple",
                "item_accessory_boom3_3_purple",

                "item_accessory_boom3_1_red",
                "item_accessory_boom3_2_red",
                "item_accessory_boom3_3_red",

                "item_accessory_boom3_1_peach",
                "item_accessory_boom3_2_peach",
                "item_accessory_boom3_3_peach"
        ));
        items.add(new Accessory("Ultimate Ears BOOM 3 Bluetooth Speaker",
                "Ultimate Ears",
                specs,
                itemStoreVariant,
                imageUris,
                true,
                true));

        /*
         * Accessory 5 - gaming laptop - HyperX QuadCast S Standalone Microphone
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 348.99),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 348.99)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "HyperX QuadCast S Standalone Microphone - Customizable RGB Lighting, Anti-Vibration, Four Polar Patterns"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        specs = new Specs(fixedSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_hyperx_quadcast_s_1_black",
                "item_accessory_hyperx_quadcast_s_2_black",
                "item_accessory_hyperx_quadcast_s_3_black"
        ));
        items.add(new Accessory("HyperX QuadCast S Standalone Microphone",
                "HyperX",
                specs,
                itemStoreVariant,
                imageUris,
                true,
                false));

        /*
         * Accessory 6 - phone - Xiaomi Mi 20000mAh 50W Fast Charging Power Bank
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 120),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 118.99),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 118.99)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Xiaomi Mi 20000mAh 50W Fast Charging Power Bank - Black. Max 50W output,Support Apple, Samsung, Xiaomi Smart phones' & Nintendo Switch Fast Charging , Charge three devices simultaneously, Support Laptop with USB-C Charging Port"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        specs = new Specs(fixedSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_xiaomi_mi_20000mah_1_black",
                "item_accessory_xiaomi_mi_20000mah_2_black",
                "item_accessory_xiaomi_mi_20000mah_3_black"
        ));
        items.add(new Accessory("Xiaomi Mi 20000mAh 50W Fast Charging Power Bank",
                "Xiaomi",
                specs,
                itemStoreVariant,
                imageUris,
                false,
                true));

        /*
         * Accessory 7 - iphone 13 pro max - Apple iPhone 13 Pro Max Leather Case with MagSafe
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("brown", "#D17B42"),
                new Colour("green", "#303D2F"),
                new Colour("midnight", "#34363A"),
                new Colour("wisteria", "#797697"),
                new Colour("cherry", "#472B3B")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 109),
                new StoreVariant("8skfdAsUs7avRyCATgRp", colours, 109),
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 109),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 109)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Apple iPhone 13 Pro Max Leather Case with MagSafe - Made with high-quality and supple leather"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        specs = new Specs(fixedSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_apple_iphone_13_pro_max_leather_case_1_brown",
                "item_accessory_apple_iphone_13_pro_max_leather_case_2_brown",
                "item_accessory_apple_iphone_13_pro_max_leather_case_3_brown",

                "item_accessory_apple_iphone_13_pro_max_leather_case_1_green",
                "item_accessory_apple_iphone_13_pro_max_leather_case_2_green",
                "item_accessory_apple_iphone_13_pro_max_leather_case_3_green",

                "item_accessory_apple_iphone_13_pro_max_leather_case_1_midnight",
                "item_accessory_apple_iphone_13_pro_max_leather_case_2_midnight",
                "item_accessory_apple_iphone_13_pro_max_leather_case_3_midnight",

                "item_accessory_apple_iphone_13_pro_max_leather_case_1_wisteria",
                "item_accessory_apple_iphone_13_pro_max_leather_case_2_wisteria",
                "item_accessory_apple_iphone_13_pro_max_leather_case_3_wisteria",

                "item_accessory_apple_iphone_13_pro_max_leather_case_1_cherry",
                "item_accessory_apple_iphone_13_pro_max_leather_case_2_cherry",
                "item_accessory_apple_iphone_13_pro_max_leather_case_3_cherry"
        ));
        items.add(new Accessory("Apple iPhone 13 Pro Max Leather Case with MagSafe",
                "Apple",
                specs,
                itemStoreVariant,
                imageUris,
                false,
                true));

        /*
         * Accessory 8 - iphone se - Lifeproof iPhone SE case
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 89),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 89)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Lifeproof iPhone SE (3rd/2nd Gen)/8/7 Fre Case - Black Lime. WATERPROOF,DIRTPROOF,SNOWPROOF,DROPPROOF(Survives drops from 6.6 feet /2 meters)"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        specs = new Specs(fixedSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_lifeproof_iphone_se_case_1_black",
                "item_accessory_lifeproof_iphone_se_case_2_black",
                "item_accessory_lifeproof_iphone_se_case_3_black"
        ));
        items.add(new Accessory("Lifeproof iPhone SE case",
                "Lifeproof",
                specs,
                itemStoreVariant,
                imageUris,
                false,
                true));

        /*
         * Accessory 9 - galaxy s22 ultra - Urban Armor Gear - Galaxy S22 Ultra 5G
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("ash", "#7E8082"),
                new Colour("black", "#000000")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("MmfBo1187Agt0n9cCl0d", colours, 65),
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 65)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Urban Armor Gear - Galaxy S22 Ultra 5G"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        specs = new Specs(fixedSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_urban_armor_gear_galaxy_s22_ultra_5g_1_ash",
                "item_accessory_urban_armor_gear_galaxy_s22_ultra_5g_2_ash",
                "item_accessory_urban_armor_gear_galaxy_s22_ultra_5g_3_ash",

                "item_accessory_urban_armor_gear_galaxy_s22_ultra_5g_1_black",
                "item_accessory_urban_armor_gear_galaxy_s22_ultra_5g_2_black",
                "item_accessory_urban_armor_gear_galaxy_s22_ultra_5g_3_black"
        ));
        items.add(new Accessory("Urban Armor Gear - Galaxy S22 Ultra 5G",
                "Urban Armor Gear",
                specs,
                itemStoreVariant,
                imageUris,
                false,
                true));

        /*
         * Accessory 10 - oneplus 10 pro - Spigen Oneplus 10 Pro Hybrid Case
         */
        colours = new ArrayList<>(Arrays.asList(
                new Colour("black", "#000000")
        ));
        itemStoreVariant = new ArrayList<>(Arrays.asList(
                new StoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 39)
        ));
        fixedSpecs = Stream.of(new String[][] {
                {SpecsType.summary.name(), "Spigen Oneplus 10 Pro Hybrid Case - Matte Black, Certified Military-Grade Protection, Clear Durable Back Panel + TPU bumper, ACS04429"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        specs = new Specs(fixedSpecs);
        imageUris = new ArrayList<>(Arrays.asList(
                "item_accessory_spigen_oneplus_10_pro_hybrid_case_1_black",
                "item_accessory_spigen_oneplus_10_pro_hybrid_case_2_black",
                "item_accessory_spigen_oneplus_10_pro_hybrid_case_3_black"
        ));
        items.add(new Accessory("Spigen Oneplus 10 Pro Hybrid Case",
                "Spigen",
                specs,
                itemStoreVariant,
                imageUris,
                false,
                true));

        return items;
    }

    private static Set<IItem> loadedAccessories, loadableAccessories;
    private static Map<String, String> itemNameToId = new HashMap<>();

    private static void onAddAccessoryComplete(IItem it) {
        loadedAccessories.add(it);

        if (loadedAccessories.equals(loadableAccessories)) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            List<IItem> items = new ArrayList<>();
            items.addAll(generateLaptops());
            items.addAll(generatePhones());
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

                for (int i = 0; i < item.getRecommendedAccessoryIds().size(); i++) {
                    String itemName = item.getRecommendedAccessoryIds().get(i);
                    item.getRecommendedAccessoryIds().set(i, itemNameToId.get(itemName));
                }

                db.collection(collectionPath).add(item).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.i("Add items to Firebase", item + " added.");
                        } else {
                            Log.i("Add items to Firebase", item + " NOT added.");
                        }
                    }
                });
            }
        }
    }

    public static void addItemsToFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        loadableAccessories = new HashSet<>(generateAccessories());
        loadedAccessories = new HashSet<>();

        for (IItem acc : loadableAccessories) {
            db.collection(CollectionPath.accessories.name()).add(acc).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        Log.i("Add items to Firebase", acc + " added.");
                    } else {
                        Log.i("Add items to Firebase", acc + " NOT added.");
                    }

                    itemNameToId.put(acc.getName(), task.getResult().getId());

                    onAddAccessoryComplete(acc);
                }
            });
        }
    }
}
