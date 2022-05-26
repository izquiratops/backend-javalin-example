# TLS

Is a cryptographic protocol designed to provide communications security over a computer network. 
It works with certifications, which can be self-signed or Certificate Authority (CA).

If all the devices are the same and well-known then a self-signed will do the job. You just need
to provide that cert for everyone, that's it 👏

BUT if you're hosting a website then you need to verify who you are and that thing is what a CA certs does.

## How to get SSL Certs
The easiest way is buying one.

The cheapest way is using Let's Encrypt. It's an organization that acts to validate the identities
of entities like websites, email addresses or even individual persons. This one is free because
(among other things) the certs we're getting from them lasts only 90 days.

There are ways to keep rolling the certs once they're expired, don't worry about it.

Let's install `certbot`, the official Let's Encrypt terminal CLI. **They have gotten rid of the apt**,
it is now:

```
snap install certbot --classic
```

From multiple ways to get the certs I think the most straight forward is with DNS Challenges.

```
certbot certonly -d my-awesome-domain.com --manual --preferred-challenges dns
```

The CLI response will provide the steps to manually update a TXT record for the domain in order
to proceed with the validation.

Once the TXT record is done you should press enter and the certificate will be issued as normally.

### Wildcards

With the example from above we could get *only* my-awesome-domain.com domain running.
In order to get certs for party.my-awesome-domain.com the same thing would have to be done again.

Wildcard certs extends the verification to any subdomain!
```
certbot certonly -d *.my-awesome-domain.com -d my-awesome-domain.com --manual --preferred-challenges dns
```

### About renewal

There's a thing called certbot hooks but I didn't tried yet 🥺

## How to setup HTTPS

Once got the certs you can find them on `/etc/letsencrypt/live/my-awesome-domain.com`.

Since Javalin is built on top of Jetty, we're setting TLS with it. Everything related can be found
on the App.java script.

### What if I'm serving with NGINX?

Everything about config files from NGINX can be found on `/etc/nginx`.
Trying to avoid a messy bunch of config files I'm setting the following structure:
- `snippets` --> Here goes the SSL-related stuff
- `snippets/certs` --> Here goes any config file that defines where the certs are stored.
- `sites-available` --> Config files for every domain we're hosting

```
# /etc/nginx/snippets/ssl.conf

ssl_session_timeout 1d;
ssl_session_cache shared:SSL:50m;
ssl_session_tickets on;

ssl_protocols TLSv1.2;
ssl_ciphers ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES256-SHA384;
ssl_ecdh_curve secp384r1;
ssl_prefer_server_ciphers on;

ssl_stapling on;
ssl_stapling_verify on;

add_header Strict-Transport-Security "max-age=15768000; includeSubdomains; preload";
add_header X-Frame-Options DENY;
add_header X-Content-Type-Options nosniff;
```

```
# /etc/nginx/snippets/certs/my-awesome-domain.com

ssl_certificate /etc/letsencrypt/live/my-awesome-domain.com/fullchain.pem;
ssl_certificate_key /etc/letsencrypt/live/my-awesome-domain.com/privkey.pem;
ssl_trusted_certificate /etc/letsencrypt/live/my-awesome-domain.com/fullchain.pem;
```

```
# /etc/nginx/sites-available/my-awesome-domain.com

server {
    # Listen default port for http
    listen 80;

    # Listen https connections
    listen 443 ssl;

    # Server name for this config
    server_name my-awesome-domain.com;

    # Include common ssl params (the first config file from above)
    include snippets/ssl.conf;

    # Include certificate params (the second config file from above)
    include snippets/certs/izquiratops.dev;

    # NO HTTP ALLOWED --> Force redirect to https
    if ($scheme != "https") {
        return 301 https://$server_name$request_uri;
    }

    location / {
        # Wherever we have our static files folder
        root        /usr/share/nginx/html;
        try_files   $uri $uri/ /index.html;
        index       index.html index.htm;
    }

}
```

# Docker

## How to create a Docker Image
Create the Image is the first step to get this project running with containers.
The docker CLI expects a dockerfile as an input, that script tells the steps to make a build.

Before running the dockerfile you had to run the tests (if there's any) and make the build.
The steps made on this dockerfile are:
- Get a docker Image ([java11 distroless](https://github.com/GoogleContainerTools/distroless))
as the environment to run our build
- Setting a working directory
- Copy build into that image
- Set something like "java -jar blabla" as the entrypoint (aka the very first thing that will run)

After all of this your image is ready, you can set a tag name and push it into Docker Hub,
this way you can fetch it from other machines.
```
    docker build -t custom-tag-image .
```

## How to run it as a Docker Container
```
    docker run --name <container name> -d -p <port in>:<port out> <image name>
```

## Portainer and Services

# Workflows & CI/CD

## How to get auto-builds from Github

## TODO: Github Actions was a mistake, how Jenkins work
