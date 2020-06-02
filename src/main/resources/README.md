This was created while trying to use a suggested approach from slashdot

https://stackoverflow.com/questions/56174997/json-schema-generator-with-inheritance-and-references

https://github.com/andifreed/json-schema-generator

My requirement was more complex, it had recursion, defaultimpl, required properties.  

I had a day free to work on this, and the first thing I notice was that it was ignoring my properties I think because they were properties instead of additional properties.  When I added the logic for additional properties to properties, they started to appear for the non recursive subclasses.

At that point the recursive property caused a stackoverflow, added checks if the class had been referenced, if so not create them item.

At this point I have to get back to my regularly scheduled tasks.  So leaving this here, and adding a comment to the stackoverflow issue.

20/6/1 

Got another task that I needed to generate a json schema for.  In addition to subtypes, this used lombok/jackson builders and had a lot of properties which were the class name of suppliers of some specific concrete type.

Also, noticed that the property description was not always present.  And wanted to have the underlying class name without the urn changes.

Finally, it wanted to report Class as a object instead of the simple string that it was.

See SchemaGeneratorTest for examples on how to use this.  I write a yaml version to build/test/reports and use the test/resource version to determine if there are changes that need to be forward to documentation.