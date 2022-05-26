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

Wildcard certs extends the verification to any subdomain too!
```
certbot certonly -d *.my-awesome-domain.com -d my-awesome-domain.com --manual --preferred-challenges dns
```

## How to setup HTTPS

# Docker

## How to create a Docker Image

## How to run it as a Docker Container

## Portainer and Services

# Workflows & CI/CD

## How to get auto-builds from Github

## TODO: Github Actions was a mistake, how Jenkins work
