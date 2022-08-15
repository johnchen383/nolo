package com.example.nolo.dataprovider;

import static com.example.nolo.repositories.category.CategoriesRepository.COLLECTION_PATH_CATEGORIES;
import static com.example.nolo.repositories.store.StoresRepository.COLLECTION_PATH_STORES;
import static com.example.nolo.repositories.user.UsersRepository.COLLECTION_PATH_USERS;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.nolo.entities.category.Category;
import com.example.nolo.entities.category.ICategory;
import com.example.nolo.entities.item.IItemVariant;
import com.example.nolo.entities.item.IPurchasable;
import com.example.nolo.entities.item.ItemVariant;
import com.example.nolo.entities.item.Purchasable;
import com.example.nolo.entities.store.Branch;
import com.example.nolo.entities.store.IStore;
import com.example.nolo.entities.store.Store;
import com.example.nolo.repositories.store.StoresRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.example.nolo.entities.user.IUser;
import com.example.nolo.entities.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

        categories.add(new Category("accessories", "category_accessory.png"));
        categories.add(new Category("phones", "category_phone.png"));
        categories.add(new Category("laptops", "category_laptop.png"));

        return categories;
    }

    public static void addCategoriesToFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<ICategory> categories = generateCategories();

        for (ICategory category : categories) {
            db.collection(COLLECTION_PATH_CATEGORIES).add(category).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.i("Add categories to Firebase", documentReference.getId() + " added.");
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
            db.collection(COLLECTION_PATH_STORES).add(store).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
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

                        db.collection(COLLECTION_PATH_USERS).document(user.getUserAuthUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
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
}
