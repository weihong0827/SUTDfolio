# SUTDfolio Documentation

  
TEAM 1F

Abram 1005057

Aditya 1005395

Beverley 1005240

Weihong 1005610  

Victoria 1005528 

Chia-Yu 1005603

Chun Hui 1005449




There are three sections to this project — [Login](#Login), [Upload](#Upload), [View](#View)

# APIRequest

This is like a SDK for the backend API which allows you to directly interact with the data without bothering about the API endpoints.

This is a Singleton Structure, meaning there can only be one instance of the object for the entire program.

To get this APIRequest instance 

```java
APIRequest requestInstance = APIRequest.getInstance();
```

using this instance you can call the different methods available

An example of the `getPosts` method

```java
requestInstance.getPosts(new Listener<String>() {
    @Override
    public void getResult(String object) {
        final Gson gson = new Gson();
        Posts[] posts = gson.fromJson(object, ReadPost[].class);
        Log.d("get",posts[0].toString());
        Toast.makeText(getActivity(),object,Toast.LENGTH_LONG).show();
    }
});
```

for all the methods, a `Listener` have to be passed into the method so that the method can handle the json response accordingly.

All the different classes that is needed have been defined for you in the models folder, you can use those classes and `gson` to parse json data into java classes and make use of the data accordingly.

Only the getPosts method have been tested so if there’s other bugs try to solve yourself if cannot then find me.

# Login

Relevant APIRequest functions: `getUser` `verify` `login`

Relevant class to take a look: `User` `LoginDataSource` `LoginFragment` `LoginViewModel`

Process of login:

1. Key In email and password
2. use the email and password and call the `login` function in the `APIRequest`
3. A Json Object will be returned, extract the `details` field
4. Ask the user for otp input, which should be sent to their registered email
5. send `email` `details` and the `otp` to the `verify` function in the `APIRequest`
6. A jwt token should be return and store it somewhere locally to be use for future tasks
7. To get the user detail pass the jwt token to the `getUser` function in `APIRequest`

# Upload

Relevant APIRequest functions: `uploadImage` `uploadPost` 

Collect the post details from the users using different input fields

upload the project image using the `uploadImage` function which takes in a string image as input and upload the image for you (Have to research on how to convert image to string, i believe using `bitstamp` or something like that) (Have try this function before so not sure if its gonna work pray hard!) the response comes back from uploadImage, parse into the `Image` class using gson and append to the CreatePost object’s image field

created a new `CreatePost` object using the information given by the user and use `gson.tojson()` function to convert the object to a json and then pass the json to the upload post function

# View

Home page should be the one with all the different posts, since already have an array of post objects, just have to render them one by one and display, might want to look into how to display image using url instead of drawable.
