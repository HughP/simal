# Introduction #

This page is intended for Admins, for those that have shell access to the Hudson Server.


## Starting, Stopping & Restarting Hudson. ##

### Starting Hudson ###

The Server has an init script written for Hudson and can be called with the command `etc/init.d/hudson start`.

This should only be needed on rare occasions, such as if Hudson was stopped manually, or if Hudson died. If the latter, one should should look into the [Log Files](MaintainingHudson#Log_Files.md) first to determine the cause.

### Stopping Hudson ###

Before stopping Hudson you should [Prepare Hudson For Shutdown](MaintainingHudson#Preparing_Hudson_For_Shutdown.md) first.
The same init script is used to stop Hudson - `/etc/init.d/hudson stop`.

You may need to stop Hudson to perform some maintenance tasks; or to Upgrade Hudson itself.

### Restarting Hudson ###

Before restarting Hudson you should [Prepare Hudson For Shutdown](MaintainingHudson#Preparing_Hudson_For_Shutdown.md) first.
Hudson is restarted by `/etc/init.d/hudson restart`.

Occasionally you may want to simply restart Hudson. For example you may be installing extra features or plugins.

### Hudson Status ###

There is one more init script feature available - `/etc/init.d/hudson status`.

Just a handy way to determine if Hudson instance is running or not, a friendlier way of doing something like `ps auxwww | grep hudson` and gives you a _hudson is started | stopped_ comfort message.

## Extra Information ##

### Log Files ###

Hudson has its own log file located at `/var/log/hudson.log`. Currently this file is appended to by Hudson continuously, so we should probably be creating a cron job to rotate this weekly/monthly.

You can look at what is currently being output the Hudson log on the server by running the command `tail -f /var/log/hudson.log &`. Any log entries from then on when also be output to your shell screen. (You can continue to do other shell duties here, but beware that log entries will interrupt your flow.) You can `fg ctrl c` out of this at any time.

### Preparing Hudson For Shutdown ###

**_Ideally_** one should prepare Hudson for being stopped or restarted. This is done on the Admin area of the Hudson web interface. Log in as an administrator, go to 'Manage Hudson' then click on the 'Prepare For Shutdown' link near the bottom of the page. This does _not_ stop any currently running builds, but it does prevent any more scheduled ones from running. Your choice now is to either wait for currently running builds to finish, or stop them manually before then logging into the server and performing a Stop or Restart.

### Upgrading Hudson ###

Upgrading Hudson should be a fairly straightforward process. Some things to consider though before deciding to upgrade.

Check the Hudson [Changelog](https://hudson.dev.java.net/changelog.html) for good reasons to upgrade such as:-

  1. Important Security Fixes
  1. Bug fixes relevant to your Hudson instance
  1. New Features and/or other improvements
  1. New Plugins not compatible with your current running version

Also be _**very sure**_ to check for new bugs introduced in later versions than you are currently running - Hudson is a very fast moving project and more bugs do creep in. So, need to keep an eye on the issue tracker and users mailing list.

Still here? Ok, so really must want to perform the upgrade.

  * Follow previous instructions on this page to [Prepare Hudson For Shutdown](MaintainingHudson#Preparing_Hudson_For_Shutdown.md)
  * and then to [Stop Hudson](MaintainingHudson#Stopping_Hudson.md).
  * On the server, navigate to /usr/local/hudson
  * Rename hudson.war to hudson.old
  * Upload or wget new release hudson.war in its place
  * Upload any updates to existing plugins
  * Follow previous instructions on this page to [Start Hudson](MaintainingHudson#Restarting_Hudson.md)