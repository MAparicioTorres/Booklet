package com.maat.booklet_tfg_v1.data.util

import com.maat.booklet_tfg_v1.data.entities.Book
import com.maat.booklet_tfg_v1.data.models.ReadingStatus

object BookSeedData {

    val predefinedBooks = listOf(
        //Creamos la lista de libros con los que precargaremos la BD.

        //Lista de libros 'Pendientes'
        Book(
            bookCoverUrl = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1672574587i/60531406.jpg",
            title = "Tress of the Emerald Sea",
            authorId = 6,
            genreId = 1,
            readingStatus = ReadingStatus.TO_READ,
            summary = "The only life Tress has known on her island home in an emerald-green ocean has been a simple one, with the simple pleasures of collecting cups brought by sailors from faraway lands and listening to stories told by her friend Charlie. But when his father takes him on a voyage to find a bride and disaster strikes, Tress must stow away on a ship and seek the Sorceress of the deadly Midnight Sea. Amid the spore oceans where pirates abound, can Tress leave her simple life behind and make her own place sailing a sea where a single drop of water can mean instant death?"
        ),
        Book(
            bookCoverUrl = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1327876792i/10644930.jpg",
            title = "11/22/63",
            authorId = 1,
            genreId = 3,
            readingStatus = ReadingStatus.TO_READ,
            summary = "On November 22, 1963, three shots rang out in Dallas, President Kennedy died, and the world changed. Unless...\n" +
                    "\n" +
                    "In 2011, Jake Epping, an English teacher from Lisbon Falls, Maine, sets out on an insane — and insanely possible — mission to prevent the Kennedy assassination.\n" +
                    "\n" +
                    "Leaving behind a world of computers and mobile phones, he goes back to a time of big American cars and diners, of Lindy Hopping, the sound of Elvis, and the taste of root beer.\n" +
                    "\n" +
                    "In this haunting world, Jake falls in love with Sadie, a beautiful high school librarian. And, as the ominous date of 11/22/63 approaches, he encounters a troubled loner named Lee Harvey Oswald..."
        ),
        Book(
            bookCoverUrl = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1565909496i/35959740.jpg",
            title = "Circe",
            authorId = 8,
            genreId = 8,
            readingStatus = ReadingStatus.TO_READ,
            summary = "In the house of Helios, god of the sun and mightiest of the Titans, a daughter is born. But Circe is a strange child--neither powerful like her father nor viciously alluring like her mother. Turning to the world of mortals for companionship, she discovers that she does possess power: the power of witchcraft, which can transform rivals into monsters and menace the gods themselves.\n"
        ),
        Book(
            bookCoverUrl = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1413706054i/18007564.jpg",
            title = "The Martian",
            authorId = 2,
            genreId = 2,
            readingStatus = ReadingStatus.TO_READ,
            summary = "Six days ago, astronaut Mark Watney became one of the first people to walk on Mars.\n" +
                    "\n" +
                    "Now, he’s sure he’ll be the first person to die there.\n" +
                    "\n" +
                    "After a dust storm nearly kills him and forces his crew to evacuate while thinking him dead, Mark finds himself stranded and completely alone with no way to even signal Earth that he’s alive—and even if he could get word out, his supplies would be gone long before a rescue could arrive.\n" +
                    "\n" +
                    "Chances are, though, he won’t have time to starve to death. The damaged machinery, unforgiving environment, or plain-old “human error” are much more likely to kill him first.\n" +
                    "\n" +
                    "But Mark isn’t ready to give up yet. Drawing on his ingenuity, his engineering skills — and a relentless, dogged refusal to quit — he steadfastly confronts one seemingly insurmountable obstacle after the next. Will his resourcefulness be enough to overcome the impossible odds against him?"
        ),
        Book(
            bookCoverUrl = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1549012300i/41223608.jpg",
            title = "Pet Sematary",
            authorId = 1,
            genreId = 5,
            readingStatus = ReadingStatus.TO_READ,
            summary = "Six days ago, astronaut Mark Watney became one of the first people to walk on Mars.\n" +
                    "\n" +
                    "Now, he’s sure he’ll be the first person to die there.\n" +
                    "\n" +
                    "After a dust storm nearly kills him and forces his crew to evacuate while thinking him dead, Mark finds himself stranded and completely alone with no way to even signal Earth that he’s alive—and even if he could get word out, his supplies would be gone long before a rescue could arrive.\n" +
                    "\n" +
                    "Chances are, though, he won’t have time to starve to death. The damaged machinery, unforgiving environment, or plain-old “human error” are much more likely to kill him first.\n" +
                    "\n" +
                    "But Mark isn’t ready to give up yet. Drawing on his ingenuity, his engineering skills — and a relentless, dogged refusal to quit — he steadfastly confronts one seemingly insurmountable obstacle after the next. Will his resourcefulness be enough to overcome the impossible odds against him?"
        ),
        Book(
            bookCoverUrl = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1696146860i/60531420.jpg",
            title = "The Sunlit Man",
            authorId = 6,
            genreId = 1,
            readingStatus = ReadingStatus.TO_READ,
            summary = "Running. Putting distance between himself and the relentless Night Brigade has been Nomad’s strategy for years. Staying one or two steps ahead of his pursuers by skipping through the Cosmere from one world to the next.\n" +
                    "\n" +
                    "But now, his powers too depleted to escape, Nomad finds himself trapped on Canticle, a planet that will kill anyone who doesn’t keep moving. Fleeing the fires of a sunrise that melts the very stones, he is instantly caught up in the struggle between a heartless tyrant and the brave rebels who defy him."
        ),
        Book(
            bookCoverUrl = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1738704267i/49552.jpg",
            title = "The Stranger",
            authorId = 7,
            genreId = 12,
            readingStatus = ReadingStatus.TO_READ,
            summary = "Published in 1942 by French author Albert Camus, The Stranger has long been considered a classic of twentieth-century literature. Le Monde ranks it as number one on its \"100 Books of the Century\" list. Through this story of an ordinary man unwittingly drawn into a senseless murder on a sundrenched Algerian beach, Camus explores what he termed \"the nakedness of man faced with the absurd.\""
        ),
        Book(
            bookCoverUrl = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1357177533i/13623848.jpg",
            title = "The Song Of Achilles",
            authorId = 8,
            genreId = 1,
            readingStatus = ReadingStatus.TO_READ,
            summary = "Achilles, \"the best of all the Greeks,\" son of the cruel sea goddess Thetis and the legendary king Peleus, is strong, swift, and beautiful, irresistible to all who meet him. Patroclus is an awkward young prince, exiled from his homeland after an act of shocking violence. Brought together by chance, they forge an inseparable bond, despite risking the gods' wrath.\n" +
                    "\n" +
                    "They are trained by the centaur Chiron in the arts of war and medicine, but when word comes that Helen of Sparta has been kidnapped, all the heroes of Greece are called upon to lay siege to Troy in her name. Seduced by the promise of a glorious destiny, Achilles joins their cause, and torn between love and fear for his friend, Patroclus follows. Little do they know that the cruel Fates will test them both as never before and demand a terrible sacrifice.\n"
        ),

        //Libro en estado 'Leyendo'
        Book(
            bookCoverUrl = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1663544598i/61884832.jpg",
            title = "All The Sinners Bleed",
            authorId = 5,
            genreId = 4,
            readingStatus = ReadingStatus.READING,
            summary = "A Black sheriff. A serial killer. A small town ready to combust.\n" +
                    "\n" +
                    "Titus Crown is the first Black sheriff in the history of Charon County, Virginia. In recent decades, Charon has had only two murders. After years of working as an FBI agent, Titus knows better than anyone that while his hometown might seem like a land of moonshine, cornbread, and honeysuckle, secrets always fester under the surface.\n" +
                    "\n" +
                    "Then a year to the day after Titus’s election, a school teacher is killed by a former student and the student is fatally shot by Titus’s deputies. Those festering secrets are now out in the open and ready to tear the town apart.\n" +
                    "\n" +
                    "As Titus investigates the shootings, he unearths terrible crimes and a serial killer who has been hiding in plain sight, haunting the dirt lanes and woodland clearings of Charon. With the killer’s possible connections to a local church and the town’s harrowing history weighing on him, Titus projects confidence about closing the case while concealing a painful secret from his own past. At the same time, he also has to contend with a far-right group that wants to hold a parade in celebration of the town’s Confederate history."
        ),

        //Lista de libros 'Leídos'
        Book(
            bookCoverUrl = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1491249244i/27877138.jpg",
            title = "It",
            authorId = 1,
            genreId = 5,
            readingStatus = ReadingStatus.READ,
            rating = 10F,
            startDate = "19/05/2025",
            endDate = "20/05/2025",
            summary = "Varios niños de una pequeña ciudad del estado de Maine se alían para combatir a una entidad diabólica que adopta la forma de un payaso y desde hace mucho tiempo emerge cada 27 años para saciarse de sangre infantil."
        ),
        Book(
            bookCoverUrl = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1597695864i/54493401.jpg",
            title = "Project Hail Mary",
            authorId = 2,
            genreId = 2,
            readingStatus = ReadingStatus.READ,
            rating = 9.50F
        ),
        Book(
            bookCoverUrl = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1733400977i/221017689.jpg",
            title = "Fundido a negro",
            authorId = 3,
            genreId = 1,
            readingStatus = ReadingStatus.READ,
            startDate = "14/01/2025",
            endDate = "11/06/2025"
        ),
        Book(
            bookCoverUrl = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1609095173i/50202953.jpg",
            title = "Piranesi",
            authorId = 4,
            genreId = 3,
            readingStatus = ReadingStatus.READ,
            rating = 8.25F
        ),

        )
}