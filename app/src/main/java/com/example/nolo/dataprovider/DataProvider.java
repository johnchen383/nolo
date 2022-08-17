package com.example.nolo.dataprovider;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.category.Category;
import com.example.nolo.entities.category.ICategory;
import com.example.nolo.entities.item.IItemVariant;
import com.example.nolo.entities.item.IPurchasable;
import com.example.nolo.entities.item.ItemVariant;
import com.example.nolo.entities.item.Purchasable;
import com.example.nolo.entities.item.Accessory;
import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.colour.IColour;
import com.example.nolo.entities.item.storevariants.IItemStoreVariant;
import com.example.nolo.entities.item.storevariants.ItemStoreVariant;
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

        categories.add(new Category("accessories", "category_accessory.png"));
        categories.add(new Category("phones", "category_phone.png"));
        categories.add(new Category("laptops", "category_laptop.png"));

        return categories;
    }

    public static void addCategoriesToFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<ICategory> categories = generateCategories();

        for (ICategory category : categories) {
            db.collection(CollectionPath.categories.name()).document(category.getCategoryType().name()).set(category).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.i("Add categories to Firebase", category.getCategoryType().name() + " added.");
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
        List<String> imageUris = new ArrayList<>();  // TODO: Haven't added images yet
        List<String> recommendedAccessoryIds = new ArrayList<>();  // TODO: Haven't other items yet
        ISpecs specs;
        List<ISpecsOption> rams = new ArrayList<>();
        List<ISpecsOption> storages = new ArrayList<>();
        List<IItemStoreVariant> itemStoreVariant = new ArrayList<>();
        List<IColour> colours = new ArrayList<>();

        colours.add(new Colour("black", "#000000"));
        colours.add(new Colour("white", "#FFFFFF"));
        colours.add(new Colour("red", "#FF0000"));
        colours.add(new Colour("green", "#00FF00"));
        colours.add(new Colour("blue", "#0000FF"));
        itemStoreVariant.add(new ItemStoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 1200));
        itemStoreVariant.add(new ItemStoreVariant("8skfdAsUs7avRyCATgRp", colours, 1500));
        itemStoreVariant.add(new ItemStoreVariant("MmfBo1187Agt0n9cCl0d", colours, 900));
        rams.add(new SpecsOption(8, 50));
        rams.add(new SpecsOption(16, 100));
        rams.add(new SpecsOption(32, 200));
        storages.add(new SpecsOption(256, 200));
        storages.add(new SpecsOption(512, 250));
        specs = new LaptopSpecs("MSI GF63 Thin 11SC GTX1650 Max Q Gaming Laptop 15.6' FHD Intel i5-11400H+HM570 8G 512G NVMe SSD GTX1650 Max-Q 4G Graphics Win11Home 1yr Warranty -WiFi6 + BT5.1, Backlight Keyboard(Red)",
                "Windows 11 Home",
                "15.6\" FHD (1920*1080) 60Hz",
                "11th Gen. Intel® Core™ i5-11400H Processor 6 Cores",
                "NVIDIA® GeForce® GTX 1650 Laptop GPU, 4GB GDDR6",
                rams,
                storages,
                "HD type (30fps @ 720p)",
                "Backlight Keyboard (Single-Color, Red)",
                "802.11 ax Wi-Fi 6 + Bluetooth v5.2",
                "2x 2W Speaker, 1x Mic-in, 1x Headphone-out",
                "N/A",
                "N/A",
                "N/A",
                "1x RJ45, 1x (4K @ 30Hz) HDMI, 1x Type-C USB3.2 Gen1, 3x Type-A USB3.2 Gen1",
                "3-Cell, 51 Battery (Whr)",
                "120W adapter",
                "359 x 254 x 21.7 mm",
                "1.86 kg");
        items.add(new Laptop("MSI GF63",
                "MSI",
                specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        colours = new ArrayList<>();
        itemStoreVariant = new ArrayList<>();
        storages = new ArrayList<>();
        colours.add(new Colour("black", "#000000"));
        colours.add(new Colour("white", "#FFFFFF"));
        colours.add(new Colour("red", "#FF0000"));
        colours.add(new Colour("green", "#00FF00"));
        itemStoreVariant.add(new ItemStoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 2000));
        itemStoreVariant.add(new ItemStoreVariant("8skfdAsUs7avRyCATgRp", colours, 1800));
        itemStoreVariant.add(new ItemStoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 2100));
        storages.add(new SpecsOption(128, 100));
        storages.add(new SpecsOption(256, 200));
        storages.add(new SpecsOption(512, 250));
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
        items.add(new Phone("Samsung Galaxy S22 Ultra",
                "Samsung",
                specs,
                itemStoreVariant,
                imageUris,
                recommendedAccessoryIds));

        colours = new ArrayList<>();
        itemStoreVariant = new ArrayList<>();
        colours.add(new Colour("black", "#000000"));
        colours.add(new Colour("silver", "#C0C0C0"));
        itemStoreVariant.add(new ItemStoreVariant("3xyrxbaFJvwdEhYcnP8g", colours, 400));
        itemStoreVariant.add(new ItemStoreVariant("8skfdAsUs7avRyCATgRp", colours, 500));
        itemStoreVariant.add(new ItemStoreVariant("MmfBo1187Agt0n9cCl0d", colours, 450));
        itemStoreVariant.add(new ItemStoreVariant("QNWnvyDQgkKf3hO0H1KN", colours, 400));
        specs = new AccessorySpecs("Sony WH-1000XM4 Wireless Over-Ear Noise-Cancelling Headphones");
        items.add(new Accessory("Sony WH-1000XM4",
                "Sony",
                specs,
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
