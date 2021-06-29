import {Style} from '../js/helper.js'

import './top-bar.js'
import './devices-list.js'

const template = document.createElement('template')
template.innerHTML = `
<div>
    <top-bar></top-bar>
    <div> <!-- class="container" -->
        <section id="devices" class="section" style="display:block">
            <devices-list></devices-list>
        </section>
    </div>
</div>
`

class MainApplication extends HTMLElement {
  constructor() {
    super()
    this.attachShadow({mode: 'open'})
    this.shadowRoot.appendChild(template.content.cloneNode(true))
    this.shadowRoot.adoptedStyleSheets = [Style.sheets.bulma] // you can add several sheets

  }

  connectedCallback() {
    this.devicesList = this.shadowRoot.querySelector("devices-list")
  }

  onDisconnected(message) {

  }

  onAuthenticated(message) {

  }
}

customElements.define('main-application', MainApplication)
