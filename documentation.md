# Project Documentation

- [Setting Up](#setting-up)
- [Git Cheatsheet](#git-cheatsheet)
- [Making Changes](#making-changes)

## Setting Up

1. Git configs
    ```bash
    git config --list # check git configs

    # add username and email (if not present)
    git config --global user.name "(your name)"
    git config --global user.email "(your email)
    ```

2. Cloning the repository
    ```bash
    git clone https://github.com/uOttawaSEGA2023/project-project-group-24.git
    cd ./project-project-group-24
    ```

## Git Cheatsheet

1. Basic commands
    ```bash
    git status # checks status of current local branch
    git diff # shows changes in files
    git log # shows commit history in current branch

    git add myFile.txt # adds a file to your staging area
    git add . # adds all files to your staging area

    git restore myFile.txt # undo local changes from file (not including staged changes)
    git restore --staged myFile.txt # removes file from staging area

    git commit -m "(message)" # commits all changes in the staging area

    git pull # pulls changes from current remote branch
    git push # pushes changes to current remote branch
    ```

2. Branching
    ```bash
    git branch # shows all local branches
    git branch -a # shows all local and remote branches

    git branch myBranch # creates a local branch
    git checkout myBranch # switches to another branch (MAKE SURE ALL YOUR CHANGES ARE COMMITED OR STASHED BEFORE SWITCHING)
    git checkout -b myBranch # creates and switches branches (2-in-1)

    git push -u origin myBranch # creates and pushes a new remote branch

    git branch -d myBranch # deletes local branch
    git push origin --delete myBranch # deletes remote branch (better to do on github)

    git stash # stashes all current changes (useful if you want to switch branches without including existing changes)
    git checkout someNewBranch
    # ...
    git checkout previousBranch
    git stash apply # reapplies all stashed changes
    git stash drop # removes latest stashed changes
    ```

## Making Changes

1. Create a new branch
    ```bash
    # branch naming convention: pd-[deliverable number]/[short description]
    # examples:
    # pd-1/create-login-form
    # pd-3/doctor-view-appointments
    # pd-999/fix-feiyus-code

    git pull
    git status # make sure you base off main branch

    git checkout -b (branch name)
    git push -u origin (branch name) # optional - setup remote branch now
    ```

2. Make changes on that branch and test to make sure things work

3. Push code to remote branch
    ```bash
    git add .
    git commit -m "(change message)"
    git push
    ```

4. Go to the github repository and make a pull request for your branch (base -> main & compare -> your branch)

5. Merge pull request to main after testing and communicating

6. If everything works, delete local & remote branches (clean up branches)
    ```bash
    git branch -d (your branch)
    git push origin --delete (your branch)
    ```
