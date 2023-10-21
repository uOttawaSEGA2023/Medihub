# project-project-group-24

## Table of Contents
- [Admin Credentials](#admin-credentials)
- [Project Deliverable 1 Checklist](#project-deliverable-1-checklist)
- [Project Deliverable 2 Checklist](#project-deliverable-2-checklist)
- [Project Deliverable 3 Checklist](#project-deliverable-3-checklist)
- [Project Deliverable 4 Checklist](#project-deliverable-4-checklist)

## Admin Credentials
- Email: Medihub043@gmail.com
- Password: admin123

## Project Deliverable 1 Checklist
- [X] The team created in GitHub classroom contains all members of the group. (10/100)
- [X] Each member of the group has made at least one commit to the repository. (20/100)
- [X] The UML Class diagram of your domain model is valid. (5/100)
- [X] The APK is submitted. (5/100)
- [X] A user can create a Patient or Doctor account. (15/100)
- [X] The Administrator, Doctor, or Patient user can see the “welcome screen” after successful authentication. The welcome screen specifies the user role. (15/100)
- [X] The user can log off. (10/100)
- [X] All fields are validated. There are appropriate error messages for incorrect inputs. (20/100)
- [X] Optional BONUS - The group uses a DB (5/100)

## Project Deliverable 2 Checklist
- [X] The updated UML Class diagram of your domain model is valid. (10/100)
- [ ] The APK is submitted. (5/100)
- [ ] When a Patient or Doctor register, their account information is stored in the DB (along with an indicator of whether their account registration has been approved, rejected, or not processed yet). (15/100)
- [ ] The Administrator can view the list of registration requests. (5/100)
- [ ] The Administrator can view the information associated with each request (the information the user entered during registration). (5/100)
- [ ] The Administrator can approve or reject a registration request. (5/100)
- [ ] If approved, the registration request disappears from the list of registration requests. (7.5/100)
- [ ] If rejected, the registration request is added to the list of rejected registration requests. (7.5/100)
- [ ] The Administrator can view the list of previously rejected registration requests. (7.5/100)
- [ ] The Administrator can approve a previously rejected request. (7.5/100)
- [ ] The registration requests are stored in the DB (10/100)
- [ ] When a Patient or Doctor attempt to login, they are either directed to the welcome page, notified that their registration request was rejected, or informed that their registration request has not been processed yet. (15/100)
- [ ] Optional BONUS - When a user registration request is approved or rejected, they receive an e-mail and notification on their phone. (5/100)

## Project Deliverable 3 Checklist
- [ ] The updated UML Class diagram of your domain model is valid. (15/100)
- [ ] The APK is submitted. (5/100)
- [ ] The Doctor can view a list of upcoming appointments. (5/100)
- [ ] The Doctor can view the information of a Patient that requested an appointment. (10/100)
- [ ] The Doctor can approve or reject an appointment request. (10/100)
- [ ] The Doctor can cancel a previously approved appointment. (10/100)
- [ ] The Doctor can view a list of past appointments. (5/100)
- [ ] The Doctor may enable a setting so that all appointment requests are automatically approved by the system without further action on their part. (10/100)
- [ ] The Doctor can view the list of upcoming shifts they are working. (10/100)
- [ ] The Doctor can add a new shift by specifying the date, start-time, and end-time of the shift. All fields must be validated. Hence, the Doctor cannot enter a date that has already passed or a shift that conflicts with another one they had previously added. (10/100)
- [ ] The Doctor can delete an existing shift. (10/100)
- [ ] Optional BONUS - The group integrates with CircleCI to see the automated builds and test unit execution. (5/100)

## Project Deliverable 4 Checklist
- [ ] The updated UML Class diagram of your domain model is valid. (10/100)
- [ ] The APK is submitted. (5/100)
- [ ] The 4 Unit test cases (simple local tests) are implemented. There is no need to include instrumentation or Espresso Tests (UI). (10/100)
- [ ] The final report is submitted (30/100)
- [ ] The application supports the requirements of Deliverables 1, 2, and 3 (In case these deliverables were not previously completed, they still need to be implemented for this deliverable) (20/100)
- [ ] The Patient can view a list of upcoming appointments. (5/100)
- [ ] The Patient can cancel an upcoming appointment. Cancellations should only be possible if the appointment is not scheduled to start in the next 60 minutes. (5/100)
- [ ] The Patient can view their past appointments. (5/100)
- [ ] The Patient can rate a Doctor with whom they previously had an appointment. (5/100)
- [ ] The Patient can search for appointments by specifying a medical specialty and selecting a time slot from the available ones. An appointment is a 30-minute time slot. (5/100)
- [ ] Once booked, the appointment appears in the Patient’s list of upcoming appointments. (5/100)
- [ ] A booked time slot is not listed when Patients look for appointments. (5/100)
- [ ] The Doctor cannot delete a shift if it is associated with one or more Patient appointments. (5/100)
- [ ] Optional BONUS - Rather than searching for appointments by specifying a medical
                       specialty, the Patient can opt to search for a Doctor by name (i.e., first name, last
                       name, or both). If the search yields only one Doctor, then that Doctor’s available
                       time slots are displayed. If the search returns multiple Doctors, then the user can
                       tap on a specific Doctor’s name to view their available time slots. The Patient can
                       then book a time slot. (5/100)
