# Travelers Diary

An Android application for the avid traveler to effortlessly keep track of their trips, including their experiences and expenses, and easily share their trips with others on the platform.

This project uses Android SDK Level 28

## Getting Setup

1. Download Android Studio: https://developer.android.com/studio
2. Clone or download this repo

    * Instructions on how to clone/download a GitHub repo: https://help.github.com/en/github/creating-cloning-and-archiving-repositories/cloning-a-repository

3. Open Android Studio and Import the project:

    ` File -> New -> Import Project`

    * Select the project folder - aka the project you just cloned/downloaded from GitHub
4. Connect to GitHub (in Android Studio):

    `File -> Preferences -> Github` and follow the steps in Android Studio
5. Link your local project to the remote project (using Android Studio)

    `VCS -> Git -> Remotes`

    * Set the remote to `git@github.com:KISS/travelers-diary.git`
    * There should be only ONE remote
6. Set up the Android Emulator, so you can simulate Android devices on your computer

    1. Make sure you have the Android Emulator installed, this can be checked under `Preferences -> Android SDK -> SDK Tools`:
        * For more information visit: https://developer.android.com/studio/intro/update#sdk-manager
    2. Install an Android virtual device (AVD) to specify the Android version and hardware characteristics of the simulated device.
        * Phone:

            Required install: Pixel 2, size 5.0"
              * 5.0" is the screen size we are optimizing for so any work done should fit and work well on this screen size.
        * System Image:

            Required download: Pie (API Level 28)
        * Accept default configuration and finish installation
        * For more information visit: https://developer.android.com/studio/run/managing-avds
    4. Run the emulator to make sure everything has been set up properly.
        * `Run -> Run App`
        * This can take several minutes, be patient and check the console in Android Studio for potential errors.
        * If the play button isn't active or you're not able to select `Run App` check the troubleshooting section at the bottom of this README.

    More detailed instructions on setting up the Android Emulator are provided at this page: https://developer.android.com/studio/run/emulator

  7. Connect to Firebase
      * `Tools -> Firebase`
      * The Firebase Assistant window will show up and you can connect through there

## Contributing

1. Verify you have the latest version of the repo

    From Android Studio:

      `VCS -> Git -> Pull`

      * A pop-up will display, make sure `origin/master` is the only option selected for "Branches to merge"

      * Click the "Pull" button

      * If you run into issues, ping the team on Slack
2. Create a separate branch (do all your work in this branch)

    From Android Studio:

      `VCS -> Git -> Branches -> New Branch`

      * The name of your branch should be descriptive. Examples below:

        `feature/map-highlighting`

        `bugfix/profile-creation`

      * Make sure "Checkout branch" is selected

      * Click "Ok" button
3. Test your work
    1. Make sure you can properly run the app using the Android Emulator
    2. Make sure the rest of the application is still working as expected.
        * If a piece of the application is broken and you're unsure why, ping the group on Slack and ask for help.
3. Track your work - Commit your changes regularly and make your commits descriptive

    * Descriptive commit examples:

      `Added MapActivity`

      `Created view for editing profile`

    * If you have several commits and want to combine them, use `git rebase` to combine the commits you want into one larger commit and keep a cleaner commit history
4. Merge your work

    1. Merge the latest changes from `origin/master` into your branch and fix any merge conflicts
    2. Once you've merged master and have no conflicts:

        * If your code could potentially affect another part of the codebase, the database, or you want someone else on the team to review your work:
          1. Push your branch to GitHub:

              * Using Android Studio: `VCS -> Git -> Push`

          2. Using GitHub, submit a pull request

              * In your pull request include all the changes you made, the JIRA ticket related to your work, and the areas of the codebase your changes might affect
          3. Request teammates as reviewers

              * Choose reviewers who are most familiar with the parts of the codebase that might be affected by your changes
          4. Ping the people you asked to review your branch on Slack to let them know you requested their review

        * If your code is fully independent from other parts of the codebase and database:
          1. Switch into the `master` branch
          2. Merge your branch with master

              * Using Android Studio:

                `VCS -> Git -> Merge Changes`

                In the Merge Branches pop-up
                * Current Branch should be set to `master`
                * Under Branches to merge, your branch should be ticked off
                * Click the "Merge" button
                * Push the updated version of `master` to the remote
                * Ping everyone on Slack to let them know you just pushed changes
                * Update any related tickets on JIRA


## Code Review

If you're asked to review someone's branch, please do so as soon as possible.

Any feedback related to syntax, implementation, or bugs should be submitted as part of your code review in GitHub. Make sure you ping the contributor on Slack to let them know you've completed your code review.

If you have questions about implementation that are more involved, ping the person on Slack and chat about the pull request in Slack or in person.

## Testing

To test someones code make sure you are referencing the correct branch.
 * If their work has been merged with `master`, you'll want to test the `master` branch.
 * If their work hasn't been merged with `master`, you will need to pull their branch down to your local environment.

To pull someone elses branch using Android Studio:

  `VCS -> Git -> Branches`

  * Under "Remote Branches" select the branch you need to test and then select "Checkout As"
  * If the branch isn't available, ping the person whose code you're testing and verify they've pushed their branch to GitHub (if they haven't pushed their branch to GitHub, it won't be available to you)

Once you've switched into the branch where testing needs to be done, launch the Android Emulator and run your tests.

If there's an active pull request associated with this test, submit your feedback in there, otherwise send your feedback on Slack in the group channel. If needed, a new JIRA ticket should be created to track the required changes.

## Troubleshooting

If in the top-middle section of the Android Studio window, you see "Edit Configurations" instead of "app", sync your project with Gradle Files

  `File -> Sync Project with Gradle Files`