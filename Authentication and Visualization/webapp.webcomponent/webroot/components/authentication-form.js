import {Style} from '../js/helper.js'

const template = document.createElement('template')
template.innerHTML = `
  <section class="hero is-small is-primary">

    <div>
        <div class="field is-horizontal mt-2">
          <div class="control mr-2">
            <input class="input is-primary" name="username" type="text" placeholder="username">
          </div>
          <div class="control mr-2">
            <input class="input is-primary" name="password" type="password" placeholder="password">
          </div>
          <div class="control mr-2">
            <h3 class="title is-3" name="message">Please, Log in</h3>
          </div>
        </div>
        <button class="button is-dark mt-2" name="btnConnect">Connect</button>
        <button class="button is-dark mt-2" name="btnDisConnect">DisConnect</button>
    </div>

  </section>
`

class AuthenticationForm extends HTMLElement {
  constructor() {
    super()
    this.attachShadow({mode: 'open'})
    this.shadowRoot.appendChild(template.content.cloneNode(true))
    this.shadowRoot.adoptedStyleSheets = [Style.sheets.bulma] // you can add several sheets
  }

  getUser() {
    return {
      username: this.fieldUserName.value,
      password: this.fieldPassword.value
    }
  }

  connectedCallback() {
    let btnConnect= this.shadowRoot.querySelector(`[name="btnConnect"]`)
    let btnDisConnect= this.shadowRoot.querySelector(`[name="btnDisConnect"]`)

    this.fieldUserName = this.shadowRoot.querySelector(`[name="username"]`)
    this.fieldPassword = this.shadowRoot.querySelector(`[name="password"]`)
    this.authenticationMessage = this.shadowRoot.querySelector(`[name="message"]`)

    btnConnect.addEventListener('click', event => {

      fetch('/authenticate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(this.getUser()),
      })
        .then(response => response.json())
        .then(data => {
          console.log('Success:', data)

          sessionStorage.setItem("smarthome-user-token", data.token)

          /*
           curl --header "content-type: application/json" \
           --header "Authorization: Bearer ${TOKEN}" \
           --url http://localhost:8080/say-hello
           */
          // get the greeting message
          fetch('/say-hello', {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${sessionStorage.getItem("smarthome-user-token")}`
            },
          })
            .then(response => response.json())
            .then(data => {
              console.log('Success:', data)
              this.authenticationMessage.innerHTML = data.greetingMessage
            })
            .catch((error) => {
              console.error('Error:', error)
              this.authenticationMessage.innerHTML = "Oups!"
            });


          this.dispatchEvent(
            new CustomEvent('is-authenticated', {
              bubbles: true, composed: true, detail: {
                name: this.fieldUserName.value
              }
            })
          )
          this.fieldUserName.value = ""
          this.fieldPassword.value = ""


        })
        .catch((error) => {
          console.error('Error:', error)
          this.authenticationMessage.innerHTML = `Bad credentials, please try again!`
          sessionStorage.clear()
        })
    })

    btnDisConnect.addEventListener('click', event => {
      console.log("dis-connect")

      fetch('/disconnect', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${sessionStorage.getItem("smarthome-user-token")}`
        }
      })
        .then(response => response.json())
        .then(data => {
          console.log('Success:', data)
          this.authenticationMessage.innerHTML = `Good Bye`

          sessionStorage.setItem("smarthome-user-token", null)

          this.dispatchEvent(
            new CustomEvent('is-disconnected', {
              bubbles: true, composed: true, detail: {}
            })
          )

        })
        .catch((error) => {
          console.error('Error:', error)
          sessionStorage.clear()
        })

    })
  }

}

customElements.define('authentication-form', AuthenticationForm)
