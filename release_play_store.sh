MASTER_BRANCH="master"
DEV_BRANCH="develop"

# Are we on the right branch?
if [ "$TRAVIS_BRANCH" = "$DEV_BRANCH" || "$TRAVIS_BRANCH" = "$MASTER_BRANCH"]; then
  
  # Is this not a Pull Request?
  if [ "$TRAVIS_PULL_REQUEST" = false ]; then
    
    # Is this not a build which was triggered by setting a new tag?
    if [ -z "$TRAVIS_TAG" ]; then
    
      jarsigner -verbose -sigalg SHA1withRSA -storepass $storepass -keypass $keypass -digestalg SHA1 -keystore key.jks app/build/outputs/apk/app-release-unsigned.apk unigrade
      zipalign -v 4 app/build/outputs/apk/app-release-unsigned.apk app/build/outputs/apk/app-release-signed.apk
      
      supply init -j api-project-202893591038-55de682e5165.json -p com.unigrade.app
      supply run -j api-project-202893591038-55de682e5165.json -p com.unigrade.app -b app/build/outputs/apk/app-release-signed.apk

      if [ "$TRAVIS_BRANCH" = "$DEV_BRANCH"]; then
      
     
      fi
    
      if [ "$TRAVIS_BRANCH" = "$MASTER_BRANCH"]; then
      
      fi


  fi
  fi
fi

