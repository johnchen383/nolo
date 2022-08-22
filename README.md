# nolo
An ecommerce mobile application build in Android Studio with Java for Software Engineering Design II (SE306)

## Commit conventions
Please use the following convention when creating commits `[type]: [msg]`

`feat` - for new features

`fix`- for bug fixes

`test`- for creating tests

`style` - for stylistic changes

`doc` - for documentation

`refactor` - for refactoring the codebase

## Use of branches
Refer to the Azure board for tickets. Branch off `main` with your associated ticket. Name your branch as `AB#XXX_some-ticket-name` where XXX is the ticket number. Ensure all branches are merged back into `main` via pull requests, approved by all other team members.


## Package structure
  .
  
    ├── ...
    ├── app/src
    │       ├── ...
    |       ├── main
    |       |       ├── ...
    |       |       ├── java/com/example/nolo 
    |       |       |        ├── ...
    |       |       |        ├── activities            #all full screen UI elements
    |       |       |        ├── enums                 #enumerations
    |       |       |        ├── fragments             #all partial screen elements, including bottom nav fragments
    |       |       |        ├── viewmodels            #associated view models to activities + fragments
    |       |       |        ├── dataprovider          #for loading data to firebase
    |       |       |        ├── entities              #entity representation
    |       |       |        ├── interactors           #domain level use cases to interface view models and repo
    |       |       |        ├── repositories          #for firebase interaction
    |       |       |        ├── util                  #for utility methods used across the application to reduce redundancy
    |       |       |        ├── adaptors              #to house adaptors which map dynamically sized lists of data to ListView/RecyclerViews
    |       |       |        └── ...
    |       |       ├── res 
    |       |       |     ├── ...
    |       |       |     ├── drawable                 #all image assets and icons, as well as associated selectors
    |       |       |     ├── dimen                    #all dimension constants
    |       |       |     ├── strings                  #all string constants
    |       |       |     ├── layout                   #all layout xml files
    |       |       |     ├── navigation               #navigation with bottom nav
    |       |       |     ├── menu                     #bottom nav xml
    |       |       |     ├── font                     #fonts
    |       |       |     └── ...
    |       |       └── ...
    |       ├── androidTest/java/com/example/nolo      #integration tests
    |       ├── test/java/com/example/nolo             #unit tests
    |       └── ...
    ├── assets_raw                                     #for all processed assets not stored in `res` directly
    └── ...
    
### Testing
Testing is done with Integration Tests for each defined repository entity
Entity specific methods (such as adding/removing from cart) are tested with Unit Tests.
GitHub actions validate unit tests and successful builds
