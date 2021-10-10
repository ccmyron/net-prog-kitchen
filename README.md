# net-prog-kitchen
Server that imitates a kitchen in a restaurant that receives orders and sends them back to the [Dining Hall](https://github.com/pelm3wka/net-prog-dining-hall)

### Prerequisites:
  * JDK 1.8
  * Maven
  * Docker

### To launch the project:
  1. Clone the code;
  2. Create a package:  ``` mvn clean package ```
  3. Build the docker image:  ``` docker build --tag=kitchen:latest . ```
  4. Launch the docker image: ``` docker run -p 9091:9091 kitchen:latest ```
 
> The kitchen should launched first
