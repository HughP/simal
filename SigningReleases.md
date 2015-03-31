# Release management and signing #

From release 0.2.3 onwards we use three techniques to sign our releases, which are largely based on the best practices at the Apache Software Foundation. There are MD5 hashing, SHA1 hashing and PGP signatures. We will discuss these types of signing in this document. The information in this document is largely based on the website of the [Apache Software Foundation](http://www.apache.org/dev/release-signing.html) and the [GnuPG manual](http://www.gnupg.org/documentation/manuals/gnupg/).

## GnuPG ##
The open source tool [GnuPG](http://www.gnupg.org) is a one-stop shop for signing binaries. All three methods described here can be used with GnuPG.

## MD5 checksum ##

MD5 is a well-known message digest algorithm that can be used to check whether the a file is corrupted. The mechanism is simple: you calculate the MD5 checksum of the file you are offering for download and put the result in a separate MD5 file. When someone is downloading the file she also downloads the MD5 and recalculates the MD5 checksum. If the checksums match, you know that the file has not been tempered with.

To create an MD5 file with GnuPG you can use the following command:

```
$ gpg --print-md MD5 simal-webapp-0.2.3.war > simal-webapp-0.2.3.war.md5
```

## SHA checksum ##

SHA is used for a family of message digest algorithms, similar to MD5. There are several algorithms like `SHA1`, `SHA256` and `SHA512` that differ in  the length of the hash key. In general it is true that the longer a hash key is, the less vulnerable the algorithm is, so it is safest to use `SHA512`. Using GnuPG, you can use this command to create the SHA file:

```
$ gpg --print-md SHA512 simal-webapp-0.2.3.war > simal-webapp-0.2.3.war.sha
```

## PGP signatures ##

In order to be able to sign files using a PGP signature, you will first have to create one. If you are new to GnuPG, a simple way to create a PGP signature is using a simply the command :

```
$ gpg --gen-key
```

More details are in the [GnuPG manual](http://www.dewinter.com/gnupg_howto/english/GPGMiniHowto-3.html).

Once you have a key and you plan to sign a release you should make your public key really public by adding it to the [KEYS file](http://code.google.com/p/simal/source/browse/trunk/simal/KEYS). This makes it possible for other people to test the integrity of the release.

The next step is to sign the release, which you can do with the following command:

```
$ gpg --armor --output simal-webapp-0.2.3.war.asc --detach-sig simal-webapp-0.2.3.war
```

This results in an ASCII file being created that contains the PGP signature of the release file. Again, see the [ASF documentation](http://www.apache.org/dev/release-signing.html) for more details.

### Checking the PGP signature ###
If you have downloaded the release and the PGP signature, and you want to make sure the signature is valid, you can also use the GnuPG tool for that purpose. First, you need to import the public keys so the tool can verify it. To that end, download the [KEYS file](http://code.google.com/p/simal/source/browse/trunk/simal/KEYS) and import it using this command:

```
$ gpg --import KEYS
```

Next, check the downloaded file and its signature using this command:

```
$ gpg --verify simal-webapp-0.2.3.war.asc simal-webapp-0.2.3.war
```

Unless you are in a [web of trust](http://en.wikipedia.org/wiki/Web_of_trust) with the person who has created the signature, this process only checks whether the release was signed with a public key that is from the project. Whether the key is indeed from the person who it states it is, needs to be checked in person. Check [Henk Penning's page](http://people.apache.org/~henkp/sig/pgp-key-signing.txt) for additional information.