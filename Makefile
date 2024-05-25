all:
	javac -d build ./src/*.java
	cd build && jar -cmf ../Manifest.mf release.jar operation

