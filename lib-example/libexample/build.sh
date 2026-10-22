mkdir linuxArm64 linuxX64 mingwX64

gcc -c example.c -I. -o example.o
ar rcs libexample.a example.o example.o

aarch64-linux-gnu-gcc -c example.c -I. -o example_arm64.o
aarch64-linux-gnu-ar rcs libexample_linux_arm64.a example_arm64.o example_arm64.o

x86_64-w64-mingw32-gcc -c example.c -I. -o example_mingw.o
x86_64-w64-mingw32-ar rcs libexample_mingw.a example_mingw.o example_mingw.o

mv libexample.a linuxX64/libexample.a
mv libexample_linux_arm64.a linuxArm64/libexample.a
mv libexample_mingw.a mingwX64/libexample.a