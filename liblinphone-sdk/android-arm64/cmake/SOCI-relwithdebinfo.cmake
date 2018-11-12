#----------------------------------------------------------------
# Generated CMake target import file for configuration "RelWithDebInfo".
#----------------------------------------------------------------

# Commands may need to know the format version.
set(CMAKE_IMPORT_FILE_VERSION 1)

# Import target "SOCI::soci_core_static" for configuration "RelWithDebInfo"
set_property(TARGET SOCI::soci_core_static APPEND PROPERTY IMPORTED_CONFIGURATIONS RELWITHDEBINFO)
set_target_properties(SOCI::soci_core_static PROPERTIES
  IMPORTED_LINK_INTERFACE_LANGUAGES_RELWITHDEBINFO "CXX"
  IMPORTED_LINK_INTERFACE_LIBRARIES_RELWITHDEBINFO "/home/huanglinqiang/android-ndk-r17/platforms/android-21/arch-arm64/usr/lib/libdl.so"
  IMPORTED_LOCATION_RELWITHDEBINFO "${_IMPORT_PREFIX}/lib/libsoci_core.a"
  )

list(APPEND _IMPORT_CHECK_TARGETS SOCI::soci_core_static )
list(APPEND _IMPORT_CHECK_FILES_FOR_SOCI::soci_core_static "${_IMPORT_PREFIX}/lib/libsoci_core.a" )

# Import target "SOCI::soci_sqlite3_static" for configuration "RelWithDebInfo"
set_property(TARGET SOCI::soci_sqlite3_static APPEND PROPERTY IMPORTED_CONFIGURATIONS RELWITHDEBINFO)
set_target_properties(SOCI::soci_sqlite3_static PROPERTIES
  IMPORTED_LINK_INTERFACE_LANGUAGES_RELWITHDEBINFO "CXX"
  IMPORTED_LINK_INTERFACE_LIBRARIES_RELWITHDEBINFO "/home/zhanghao/cole/vendor/customer/gome_src/BeautyMirror_IUV/liblinphone-sdk/android-arm64/lib/libsqlite3.a;/home/zhanghao/cole/vendor/customer/gome_src/BeautyMirror_IUV/liblinphone-sdk/android-arm64/lib/libsqlite3.a"
  IMPORTED_LOCATION_RELWITHDEBINFO "${_IMPORT_PREFIX}/lib/libsoci_sqlite3.a"
  )

list(APPEND _IMPORT_CHECK_TARGETS SOCI::soci_sqlite3_static )
list(APPEND _IMPORT_CHECK_FILES_FOR_SOCI::soci_sqlite3_static "${_IMPORT_PREFIX}/lib/libsoci_sqlite3.a" )

# Commands beyond this point should not need to know the version.
set(CMAKE_IMPORT_FILE_VERSION)
