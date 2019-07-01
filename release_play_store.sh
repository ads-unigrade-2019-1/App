MASTER_BRANCH="master"
DEV_BRANCH="develop"

# Are we on the right branch?
if [  "$TRAVIS_BRANCH" = "$DEV_BRANCH" ] || [ "$TRAVIS_BRANCH" = "$MASTER_BRANCH" ]; then
  
  # Is this not a Pull Request?
  if [ "$TRAVIS_PULL_REQUEST" = false ]; then
    
    # Is this not a build which was triggered by setting a new tag?
    if [ -z "$TRAVIS_TAG" ]; then
    
      jarsigner -verbose -sigalg SHA1withRSA -storepass $storepass -keypass $keypass -digestalg SHA1 -keystore keys/keys.jks app/build/outputs/apk/release/app-release-unsigned.apk unigrade
      $ANDROID_HOME/build-tools/28.0.3/zipalign -v 4 app/build/outputs/apk/release/app-release-unsigned.apk app/build/outputs/apk/release/app-release-signed.apk
      
      fastlane supply init -j keys/api-project-202893591038-55de682e5165.json -p com.unigrade.app
    
      if [ "$TRAVIS_BRANCH" = "$DEV_BRANCH" ]; then
        fastlane supply run -j keys/api-project-202893591038-55de682e5165.json -p com.unigrade.app -b app/build/outputs/apk/release/app-release-signed.apk --track beta
      fi
    
      if [ "$TRAVIS_BRANCH" = "$MASTER_BRANCH" ]; then
        fastlane supply run -j keys/api-project-202893591038-55de682e5165.json -p com.unigrade.app -b app/build/outputs/apk/release/app-release-signed.apk -- track production
      fi
    fi
  fi
fi

