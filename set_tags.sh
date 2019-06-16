MASTER_BRANCH="master"
DEV_BRANCH="deploy"

# Are we on the right branch?
if [  "$TRAVIS_BRANCH" = "$DEV_BRANCH" ] || [ "$TRAVIS_BRANCH" = "$MASTER_BRANCH" ]; then
  
  # Is this not a Pull Request?
  if [ "$TRAVIS_PULL_REQUEST" = false ]; then
    
    # Is this not a build which was triggered by setting a new tag?
    if [ -z "$TRAVIS_TAG" ]; then
      echo -e "Starting to tag commit.\n"

      git config --global user.email "travis@travis-ci.org"
      git config --global user.name "Travis"

      # Add tag and push to master.
    
      if [ "$TRAVIS_BRANCH" = "$DEV_BRANCH" ]; then
      
        git tag -a v${TRAVIS_BUILD_NUMBER} -m "Travis build $TRAVIS_BUILD_NUMBER pushed a tag from develop."
      
      fi
    
      if [ "$TRAVIS_BRANCH" = "$MASTER_BRANCH" ]; then
      
        git tag -a v${TRAVIS_BUILD_NUMBER} -m "Travis build $TRAVIS_BUILD_NUMBER pushed a tag from master."
      
      fi

      git push origin --tags
      git fetch origin

      echo -e "Done magic with tags.\n"
  fi
  fi
fi