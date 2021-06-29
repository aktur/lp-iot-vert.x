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

  }

  connectedCallback() {
    let btnConnect= this.shadowRoot.querySelector(`[name="btnConnect"]`)
    let btnDisConnect= this.shadowRoot.querySelector(`[name="btnDisConnect"]`)

    this.fieldUserName = this.shadowRoot.querySelector(`[name="username"]`)
    this.fieldPassword = this.shadowRoot.querySelector(`[name="password"]`)
    this.authenticationMessage = this.shadowRoot.querySelector(`[name="message"]`)

  }

}

customElements.define('authentication-form', AuthenticationForm)
