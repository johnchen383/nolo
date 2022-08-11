# nolo
An ecommerce mobile application build in Android Studio with Java for Software Engineering Design II (SE306)

## Commit conventions
Please use the following convention when creating commits `[type]: [msg]`

`AB#{id} feat` - for new features

`AB#{id} fix`- for bug fixes

`AB#{id} test`- for creating tests

`AB#{id} style` - for stylistic changes

`AB#{id} doc` - for documentation

`AB#{id} refactor` - for refactoring the codebase

## Use of branches
Refer to the Jira board for tickets. Branch off `main` with your associated ticket. Name your branch as `NLXXX_some-ticket-name` where XXX is the ticket number. Ensure all branches are merged back into `main` via pull requests, approved by all other team members.


## Package structure
```
- /src/main/java/com/nolo
  - /activities [all full screen UI elements]
  - /fragments [all partial screen elements, including bottom nav fragments]
  - /viewmodels [associated view models to activities + fragments]
 
- /res
  - /drawable [all image assets and icons, as well as associated selectors]
  - /dimen [all dimension constants]
  - /strings [all string constants]
  - /layout [all layout xml files]
  - /navigation [navigation with bottom nav]
  - /menu [bottom nav xml]
  
- /assets_raw [for all processed assets not stored in `res` directly]
```
