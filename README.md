Phoenix CTMS
=====

Phoenix CTMS is a large web application combining capabilities of database software used in clinical research in one modular system.

* __PRS__ (Patient Recruitment System)
* __CTMS__ (Clinical Trial Management System)
* __CDMS__ (Clinical Data Management System)

This unmatched feature set is geared to support all operational and regulatory requirements of the clinical front end in academic research, at CROs (Contract Research Organisations) and hospitals conducting clinical studies of any phase.

![alt text](http://phoenixctms.org/wp-content/uploads/2017/08/modules_en.png)

After years of collaborative development with trial sites at the Medical University of Graz, the Phoenix CTMS now becomes publicly available (LGPL 2.1). It could be the perfect choice if you e.g.

* need a private, _encrypted_ subject database for PII (personally identifyable information), to complement your existing CDMS
* need a solution to comply with upcoming _EU-GDPR_ (General Data Protection Regulation)
* want to operate a secure online _signup portal_ for subject candidates
* want a CDMS that gives you unlimited _Javascript_ form scripting support (server- and browserside)
* need to deal with _large_ eCRFs (electronic case report forms)
* want to try a serious _OpenClinica_ alternative
* need to formulate complex _ad-hoc_ database queries to list matching subject candidates using _set operations_
* conduct several trials in parallel and need to organize site staff and resources
* need a software to implement various processes for ICH GCP (good clinical practice) compliance
* want a turn-key system that’s operational out-of-the-box, instead of integrating multiple systems

This GitHub repository contains all tiers of the main JEE web application. Transient artifacts generated using AndroMDA are excluded.

Build and Install
-----
1. Prepare a vanilla _Debian Stretch_ Linux instance (ie. from [debian-9.2.1-amd64-netinst.iso](https://cdimage.debian.org/debian-cd/current/amd64/iso-cd/debian-9.2.1-amd64-netinst.iso)):
  * 2 vCPUs, 4-8 GB RAM and 10-20 GB disk should be fine for a test environment
  * Select basic packages only (no database or webserver)

  The procedure was also successfully tested with _Ubuntu 16.04_ (eg. as provided by Amazon E2C ami-2581aa40).

2. Open a terminal and run the installer to automatically download, build and configure your Phoenix CTMS:

   ```bash
   sudo apt-get -y install wget ca-certificates
   wget https://raw.githubusercontent.com/phoenixctms/install-debian/master/install.sh -O ~/install.sh
   chmod 744 ~/install.sh
   sudo ~/install.sh
   ```

   The procedure will require internet connectivity to download from debian, maven, cpan mirros and github. It will take around 1-2 hours to complete.

3. You can now open a browser and log into the system using the link and credentials printed at last.
4. If desired, populate the system with demo data:

   ```bash
   sudo -u ctsms /ctsms/dbtool.sh --load_demo_data
   ```