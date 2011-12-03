# Play postmark module

This is a [play](http://www.playframework.org/) module for integrating [Postmark](http://postmarkapp.com) to handle outgoing e-mail in 
you play application. 

## About Postmark

> Postmark enables web applications of any size to deliver and track transactional email 
> reliably, with minimal setup time and zero maintenance. We're the experts at getting 
> your emails to the inbox, so you don't have to be. 

## Getting started

This module does not currently exist in play's module repository.

### Download and build locally

You can download the source and build the module locally:

    $ git clone git@github.com:FrostDigital/play-postmark.git
    $ cd play-postmark
    $ play build-module

The module should now be built and you can add it in your **dependencies.yml** by adding a local module repo.

### Use Frost's module repo

Add Frosts module repo to your **dependencies.yml**:
	
	require:
        - postmark -> postmark 0.1.1

    repositories:
	    - frost:
	        type: http
	        artifact: "http://frostdigital.github.com/module-repo/[module]-[revision].zip"
	        contains:
	          - postmark

Now download and sync new deps:

    $ play deps --sync 

## Configuration

Place props in application.conf

    # Your API key from postmark, use POSTMARK_API_TEST 
    # to test the service without sending any mails (required!)
    play.postmark.apiKey=POSTMARK_API_TEST

    # Default "from" fields in sent mails
	# Note: this e-mail must be a sender signature in postmarkapp.com
    play.postmark.from=darth@space.com

## Usage

    // to, subject, body
    Postmark.sendMail("luke@space.com", "How's life?", "Just wanted to check. Hope you are well!")


