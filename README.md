This was created while trying to use a suggested approach from slashdot

https://stackoverflow.com/questions/56174997/json-schema-generator-with-inheritance-and-references

My requirement was more complex, it had recursion, defaultimpl, required properties.  

I had a day free to work on this, and the first thing I notice was that it was ignoring my properties I think because they were properties instead of additional properties.  When I added the logic for additional properties to properties, they started to appear for the non recursive subclasses.

At that point the recursive property caused a stackoverflow, added checks if the class had been referenced, if so not create them item.

At this point I have to get back to my regularly scheduled tasks.  So leaving this here, and adding a comment to the stackoverflow issue.