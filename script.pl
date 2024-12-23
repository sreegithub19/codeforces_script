#!/usr/bin/perl
use strict;
use warnings;

my $multi_line_string = q{
This is a multi-line string.
It spans multiple lines.
No variable interpolation happens here.
};

print $multi_line_string;