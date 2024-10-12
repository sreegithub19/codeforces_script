#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct Contact {
    char name[50];
    char phone[15];
    struct Contact *next;
} Contact;

Contact *head = NULL;

// Function prototypes
void addContact(const char *name, const char *phone);
void displayContacts();
void freeContacts();

int main() {
    // Initialize the address book with sample data
    addContact("Alice Johnson", "123-456-7890");
    addContact("Bob Smith", "987-654-3210");
    addContact("Charlie Brown", "555-555-5555");

    // Display the contacts
    displayContacts();

    // Free the allocated memory
    freeContacts();
    return 0;
}

void addContact(const char *name, const char *phone) {
    Contact *newContact = (Contact *)malloc(sizeof(Contact));
    if (newContact == NULL) {
        printf("Memory allocation failed!\n");
        return;
    }
    strcpy(newContact->name, name);
    strcpy(newContact->phone, phone);
    newContact->next = head;
    head = newContact;
}

void displayContacts() {
    if (head == NULL) {
        printf("No contacts available.\n");
        return;
    }

    Contact *current = head;
    printf("\n--- Contacts ---\n");
    while (current != NULL) {
        printf("Name: %s, Phone: %s\n", current->name, current->phone);
        current = current->next;
    }
}

void freeContacts() {
    Contact *current = head;
    while (current != NULL) {
        Contact *next = current->next;
        free(current);
        current = next;
    }
    head = NULL;
}
